package monitoring.tools;

import java.io.IOException;
import java.io.InputStream;
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

import org.json.JSONArray;
import org.json.JSONObject;

import kafka.javaapi.producer.Producer;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringData;
import monitoring.model.MonitoringParams;
import monitoring.services.ToolInterface;

public class AppTweak implements ToolInterface {
	
	//Token credentials
	private final String token = "iOAbyjaOnWFNpO64RCVnG3TWmR4";
	
	//Route params
	private final String uri = "https://api.apptweak.com/android/applications/";
	private final String uriParams = "/reviews.json?country=es&language=es";
	
	private MonitoringParams params;
	
	private boolean firstConnection = true;
	
	private int id = 1;
	
	//Kafka producer
	Producer<String, String> producer;
	
	Map<String, String> reported;

	@Override
	public void addConfiguration(MonitoringParams params, Producer<String, String> producer) throws Exception {
		
		this.params = params;
		this.producer = producer;
		this.reported = new HashMap<>();
		
		firstApiCall();
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    public void run() {
		    	if (firstConnection) {
					firstConnection = false;
					System.out.println("First connection stablished");		
		    	} else {
					try {
						apiCall();
					} catch (IOException e) {
						e.printStackTrace();
					}	    		
		    	}
		    }

		}, 0, Integer.parseInt(params.getTimeSlot())* 1000);
		
	}

	private void firstApiCall() throws MalformedURLException, IOException {
		
		JSONObject data = urlConnection();
		
		JSONArray reviews = data.getJSONArray("content");
		for (int i = 0; i < reviews.length(); ++i) {
			reported.put(reviews.getJSONObject(i).getString("id"), reviews.getJSONObject(i).getString("date"));
		}
		
	}

	protected void apiCall() throws MalformedURLException, IOException {
		
		JSONObject data = urlConnection();
		
		String timeStamp = new Timestamp((new Date()).getTime()).toString();
		
		List<MonitoringData> dataList = new ArrayList<>();
		JSONArray reviews = data.getJSONArray("content");
		for (int i = 0; i < reviews.length(); ++i) {
			
			JSONObject obj = reviews.getJSONObject(i);
						
			if (!reported.containsKey(obj.getString("id"))
					|| (reported.containsKey(obj.getString("id")) 
					&& !reported.get(obj.getString("id")).equals(obj.getString("date")))) {
				
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
				reported.put(obj.getString("id"), obj.getString("date"));
			}
		}
		KafkaCommunication.generateResponse(dataList, timeStamp, producer, id, params.getKafkaTopic());
		++id;
	}
	
	private JSONObject urlConnection() throws MalformedURLException, IOException {
		URLConnection connection = new URL(uri + params.getPackageName() + uriParams)
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

}
