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

public class GooglePlayAPI implements ToolInterface {
	
	final static Logger logger = Logger.getLogger(GooglePlayAPI.class);
	
	private int confId;
	
	//OAuth credentials for API authentication
	private final String 	clientID = "785756325088-kes5s8om6n4o6sntvokqjt47tito7mla.apps.googleusercontent.com";
	private final String 	clientSecret = "hr5Fcj-Awp8XejMWbxBBI6ps";
	private final String 	refreshToken = "1/MKGHOAO4tTvO2kGECyG_EDUroJ07v55aIV_3EBbvKGA";
	//API Uri
	private final String 	apiUri = "https://www.googleapis.com/androidpublisher/v2/applications/";
	private final String	tokenUri = "https://accounts.google.com/o/oauth2/token";
	//Access token for API authentication
	private String 		 	accessToken = "ya29.Ci8WA5RBcItol_FE18VAHwHuueraVng6wdVi8kma8mDSRpQZtbX2XDM7cHbuMT0rnA";
	
	private boolean 		firstConnection = true;
	
	//temporal fake id
	private int				id = 1;
	
	private Date initTime;
	private Date stamp;
	
	private MonitoringParams params;
	
	//Kafka producer
	private Producer<String, String> producer;
	
	private Timer timer;
	
	@Override
	public void addConfiguration(MonitoringParams params, Producer<String, String> producer, int confId) throws Exception {

		this.params = params;
		this.producer = producer;
		this.confId = confId;
		
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
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}
	
	protected JSONArray getNextPage(String token) throws MalformedURLException, IOException {
		URLConnection connection = new URL(apiUri + params.getPackageName() + "/reviews" 
				+ "?access_token=" + accessToken
				+ "&maxResults=100"
				+ "&token=" + token)
				.openConnection();
		
		JSONObject data = new JSONObject(streamToString(connection.getInputStream()));
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
		
		JSONObject res = new JSONObject(streamToString(httpConnection.getInputStream()));
		accessToken = res.getString("access_token");
		
	}

	/*
	 * When data is successfully retrieved, json response is generated
	 */
	protected void generateData(InputStream response, long date) throws MalformedURLException, JSONException, IOException {

		JSONObject data = new JSONObject(streamToString(response));
		
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
			Long l = getDateTimeInMillis(obj);
						
			//Check if the review has already been reported;
			//if so, check if the last update was reported;
			//if it wasn't, check if the review datetime is later than the initMonitorization time
			//if so, report the review
			if (l.compareTo(stamp.getTime()) > 0) {
				Iterator<?> keys = obj.keys();
				MonitoringData review = new MonitoringData();
				
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
		
		KafkaCommunication.generateResponse(dataList, timeStamp, producer, id, confId, params.getKafkaTopic());
		logger.debug("Data sent to kafka endpoint");
		++id;
		
	}

	private String streamToString(InputStream stream) {
		StringBuilder sb = new StringBuilder();
		try (Scanner scanner = new Scanner(stream)) {
		    String responseBody = scanner.useDelimiter("\\A").next();
		    sb.append(responseBody);
		}
		return sb.toString();
	}
	
	private long getDateTimeInMillis(JSONObject obj) {
		return obj.getJSONArray("comments").getJSONObject(0).getJSONObject("userComment")
		.getJSONObject("lastModified").getInt("nanos")/1000000 + 
		obj.getJSONArray("comments").getJSONObject(0).getJSONObject("userComment")
		.getJSONObject("lastModified").getLong("seconds")*1000;
	}

}