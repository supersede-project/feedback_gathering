/*******************************************************************************
 * Copyright (c) 2016 Universitat Politécnica de Catalunya (UPC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Quim Motger (UPC) - main development
 * 	
 * Initially developed in the context of SUPERSEDE EU project
 * www.supersede.eu
 *******************************************************************************/
package monitoring.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kafka.javaapi.producer.Producer;
import monitoring.controller.ToolInterface;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.GooglePlayMonitoringData;
import monitoring.model.GooglePlayMonitoringParams;
import monitoring.model.MonitoringData;
import monitoring.model.MonitoringParams;
import monitoring.model.Utils;

public class GooglePlayAPI implements ToolInterface<GooglePlayMonitoringParams> {
	
	final static Logger logger = Logger.getLogger(GooglePlayAPI.class);
	
	private int confId;
	
	//OAuth credentials for API authentication
	private String 	clientID;
	private String 	clientSecret;
	private String 	refreshToken;
	private String 	accessToken;

	private final String 	apiUri = "https://www.googleapis.com/androidpublisher/v2/applications/";
	private final String	tokenUri = "https://accounts.google.com/o/oauth2/token";
	
	private boolean 		firstConnection = true;
	private int				id = 1;
	
	private Date initTime;
	private Date stamp;
	
	private GooglePlayMonitoringParams params;
	
	private Timer timer;
	
	KafkaCommunication kafka;
	
	@Override
	public void addConfiguration(GooglePlayMonitoringParams params, int confId) throws Exception {
		this.params = params;
		this.confId = confId;
		this.kafka = new KafkaCommunication();
		
		loadProperties();
		resetStream();
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}
	
	@Override
	public void updateConfiguration(GooglePlayMonitoringParams params) throws Exception {
		deleteConfiguration();
		this.params = params;
		resetStream();
	}
	
	private void resetStream() throws Exception  {
		
		//this.kafka.initProducer(this.params.getKafkaEndpoint());
		this.kafka.initProxy();
		
		firstConnection = true;
		generateNewAccessToken();
		timer = new Timer();
		timer.schedule(new TimerTask() {
		    public void run() {
		    	if (firstConnection) {
		    		logger.debug("Connection established");
		    		initTime = new Date();
					firstConnection = false;
		    	} else {
		    		stamp = initTime;
		    		initTime = new Date();
					try {
						apiCall();
					} catch (IOException e) {
						logger.debug("Invalid access token. Generating a new one");
						try {
							generateNewAccessToken();
							apiCall();
						} catch (IOException e1) {
							logger.error("There was an unexpected error with the API call");
						}
					}		    		
		    	}
		    }
		}, 0, Integer.parseInt(params.getTimeSlot())* 1000);
	}
	
	protected JSONArray getNextPage(String token) throws MalformedURLException, IOException {
		URLConnection connection = new URL(apiUri + params.getPackageName() + "/reviews" 
				+ "?access_token=" + accessToken
				+ "&maxResults=100"
				+ "&token=" + token)
				.openConnection();
		JSONObject data = new JSONObject(Utils.streamToString(connection.getInputStream()));
		JSONArray reviews = data.getJSONArray("reviews");
		if (data.has("tokenPagination")) {
			JSONArray next = getNextPage(data.getJSONObject("tokenPagination")
					.getString("nextPageToken"));
			for (int i = 0; i < next.length(); ++i) reviews.put(next.get(i));
		} 
		return reviews;
	}

	protected void apiCall() throws MalformedURLException, IOException {
		URLConnection connection = new URL(apiUri + params.getPackageName() + "/reviews" 
				+ "?access_token=" + accessToken
				+ "&maxResults=100")
				.openConnection();
		generateData(connection.getInputStream(), (new Date()).getTime());
	}

	/*
	 * Generates a new access token using the refresh token
	 */
	protected void generateNewAccessToken() throws MalformedURLException, IOException {
		
		String query = "grant_type=refresh_token"
				+ "&client_id=" + clientID 
				+ "&client_secret=" + clientSecret 
				+ "&refresh_token=" + refreshToken;
		URLConnection httpConnection = new URL(tokenUri 
				+ "?" + query)
				.openConnection();
		httpConnection.setDoOutput(true);
		//httpConnection.setFixedLengthStreamingMode(0);
		try (OutputStream output = httpConnection.getOutputStream()) {
		    output.write(query.getBytes("UTF-8"));
		}
		JSONObject res = new JSONObject(Utils.streamToString(httpConnection.getInputStream()));
		accessToken = res.getString("access_token");
		
	}

	/*
	 * When data is successfully retrieved, json response is generated
	 */
	protected void generateData(InputStream response, long date) throws MalformedURLException, JSONException, IOException {

		JSONObject data = new JSONObject(Utils.streamToString(response));
		JSONArray reviews = data.getJSONArray("reviews");
		if (data.has("tokenPagination")) {
			JSONArray next = getNextPage(data.getJSONObject("tokenPagination")
					.getString("nextPageToken"));
			for (int i = 0; i < next.length(); ++i) {
				reviews.put(next.get(i));
			}
		}
		
		List<MonitoringData> dataList = new ArrayList<>();
		for (int i = 0; i < reviews.length(); ++i) {
			JSONObject obj = reviews.getJSONObject(i);
			//review time in milliseconds
			Long l = Utils.getDateTimeInMillis(obj);
			//Check if the review has already been reported;
			//if so, check if the last update was reported;
			//if it wasn't, check if the review datetime is later than the initMonitorization time
			//if so, report the review
			if (l.compareTo(stamp.getTime()) > 0) {
				Iterator<?> keys = obj.keys();
				GooglePlayMonitoringData review = new GooglePlayMonitoringData();
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if (key.equals("reviewId")) review.setReviewID(obj.getString("reviewId"));
				    else if (key.equals("authorName")) review.setAuthorName(obj.getString("authorName"));
				    else if (key.equals("comments")) {
				    	JSONObject userComment = obj.getJSONArray("comments").getJSONObject(0)
				    			.getJSONObject("userComment");
				    	Iterator<?> keysComment = userComment.keys();
				    	while (keysComment.hasNext()) {
				    		String keyComment = (String)keysComment.next();
				    		if (keyComment.equals("text")) review.setReviewText(userComment.getString("text"));
						    else if (keyComment.equals("starRating")) review.setStarRating(String.valueOf(userComment.getInt("starRating")));
						    else if (keyComment.equals("reviewerLanguage")) review.setReviewerLanguage(userComment.getString("reviewerLanguage"));
						    else if (keyComment.equals("device")) review.setDevice(userComment.getString("device"));
						    else if (keyComment.equals("appVersionName")) review.setAppVersion(userComment.getString("appVersionName"));
				    	}
				    }
				}
				review.setTimeStamp(new Timestamp(l).toString());
				dataList.add(review);
			}
		}
		String timeStamp = new Timestamp(date).toString();
		//kafka.generateResponseKafka(dataList, timeStamp, id, confId, params.getKafkaTopic());
		kafka.generateResponseIF(dataList, timeStamp, id, confId, params.getKafkaTopic(), "GooglePlayMonitoredData");
		logger.debug("Data sent to kafka endpoint");
		++id;
	}
	
	private void loadProperties() throws Exception {
		Properties prop = new Properties();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream("config.properties");
			prop.load(input);
			clientID = prop.getProperty("clientID");
			clientSecret = prop.getProperty("clientSecret");
			refreshToken = prop.getProperty("refreshToken");
			accessToken = prop.getProperty("accessToken");
		} catch (Exception e) {
			throw new IOException("There was an unexpected error loading the properties file.");
		}
	}

}
