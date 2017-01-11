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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kafka.javaapi.producer.Producer;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringData;
import monitoring.model.MonitoringParams;
import monitoring.services.ToolInterface;

public class AppTweak implements ToolInterface {
	
	final static Logger logger = Logger.getLogger(AppTweak.class);
	
	private int confId;
	
	//Token credentials
	private final String token = "iOAbyjaOnWFNpO64RCVnG3TWmR4";
	
	//Route params
	private final String uri = "https://api.apptweak.com/android/applications/";
	private final String uriParams = "/reviews.json";
	
	private MonitoringParams params;
	
	private boolean firstConnection = true;
	
	private int id = 1;
	
	private Date initTime;
	private Date stamp;
	
	private Timer timer;
	
	//Kafka producer
	Producer<String, String> producer;

	@Override
	public void addConfiguration(MonitoringParams params, Producer<String, String> producer, int confId) throws Exception {
		
		this.params = params;
		this.producer = producer;
		this.confId = confId;
		
		resetStream();
		
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}

	protected void apiCall() throws MalformedURLException, IOException, JSONException, ParseException {
		
		String timeStamp = new Timestamp((new Date()).getTime()).toString();
		
		JSONObject data = urlConnection();
		
		List<MonitoringData> dataList = new ArrayList<>();
		JSONArray reviews = data.getJSONArray("content");
		for (int i = 0; i < reviews.length(); ++i) {
			
			JSONObject obj = reviews.getJSONObject(i);
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
			Date date = format.parse(obj.getString("date"));
						
			if (date.compareTo(stamp) > 0) {
				
				Iterator<?> keys = obj.keys();
				MonitoringData review = new MonitoringData();
				
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if (key.equals("id")) review.setReviewID(obj.getString("id"));
				    else if (key.equals("author")) review.setAuthorName(obj.getJSONObject("author").getString("name"));
				    else if (key.equals("title")) review.setReviewTitle(obj.getString("title"));
				    else if (key.equals("body")) review.setReviewText(obj.getString("body"));
				    else if (key.equals("date")) review.setTimeStamp(obj.getString("date"));
				    else if (key.equals("rating")) review.setStarRating(String.valueOf(obj.getInt("rating")));
				    else if (key.equals("version")) review.setAppVersion(obj.getString("version"));
				}
				
				dataList.add(review);
			}
		}
		KafkaCommunication.generateResponse(dataList, timeStamp, producer, id, confId, params.getKafkaTopic());
		logger.debug("Data sent to kafka endpoint");
		++id;
	}
	
	private JSONObject urlConnection() throws MalformedURLException, IOException {
		String URI = uri + params.getPackageName() + uriParams;
		boolean first = true;
		if (params.getCountry() != null) {
			URI += "?country=" + params.getCountry();
			first = false;
		}
		if (params.getLanguage() != null) {
			if (first) URI += "?";
			else URI += "&";
			URI += "language=" + params.getLanguage();
		}
		URLConnection connection = new URL(URI)
				.openConnection();
		connection.setRequestProperty("X-Apptweak-Key", token);
		connection.getInputStream();
		
		return new JSONObject(streamToString(connection.getInputStream()));
	}
	
	private String streamToString(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		try (Scanner scanner = new Scanner(stream)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    sb.append(responseBody);
		}
		return sb.toString();
	}

	@Override
	public void updateConfiguration(MonitoringParams params) throws Exception {
		deleteConfiguration();
		apiCall();
		this.params = params;
		resetStream();
	}

	private void resetStream() {
		firstConnection = true;
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
						logger.error("The API call was not correctly build");
					} catch (JSONException|ParseException e) {
						logger.error("The response provided by the API tool was not a valid JSON object");
					}		
		    	}
		    }

		}, 0, Integer.parseInt(params.getTimeSlot())* 1000);
	}

}
