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

public class ITunesApple implements ToolInterface {
	
	private int confId;
	
	private String uri = "https://itunes.apple.com/es/rss/customerreviews/id=";
	private String uriParams = "/sortBy=mostRecent/json";

	private MonitoringParams params;
	private Producer<String, String> producer;
	private List<String> reported;
	
	private int id = 1;
	
	private boolean firstConnection = false;
	
	private Timer timer;

	@Override
	public void addConfiguration(MonitoringParams params, Producer<String, String> producer, int confId) throws Exception {
		
		this.params = params;
		this.producer = producer;
		this.reported = new ArrayList<>();
		this.confId = confId;
		
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
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}

	protected void apiCall() throws IOException, MalformedURLException {
		
		String timeStamp = new Timestamp((new Date()).getTime()).toString();
		
		JSONObject data = urlConnection();
		
		JSONArray reviews = data.getJSONObject("feed").getJSONArray("entry");
		List<MonitoringData> dataList = new ArrayList<>();
		
		for (int i = 1; i < reviews.length(); ++i) {
			JSONObject obj = reviews.getJSONObject(i);
			
			String id = obj.getJSONObject("id").getString("label");
			
			if (!reported.contains(id)) {
			
				Iterator<?> keys = obj.keys();
				MonitoringData review = new MonitoringData();
				review.setReviewID(id);
				
				while( keys.hasNext() ) {
				    String key = (String)keys.next();
				    if (key.equals("author")) review.setAuthorName(obj.getJSONObject("author").getJSONObject("name").getString("label"));
				    else if (key.equals("title")) review.setReviewTitle(obj.getJSONObject("title").getString("label"));
				    else if (key.equals("content")) review.setReviewText(obj.getJSONObject("content").getString("label"));
				    else if (key.equals("im:rating")) review.setStarRating(obj.getJSONObject("im:rating").getString("label"));
				    else if (key.equals("im:version")) review.setAppVersion(obj.getJSONObject("im:version").getString("label"));
				    else if (key.equals("link")) review.setLink(obj.getJSONObject("link").getJSONObject("attributes").getString("href"));
				}
				
				dataList.add(review);
				reported.add(id);
			}
		}
		
		KafkaCommunication.generateResponse(dataList, timeStamp, producer, id, confId, params.getKafkaTopic());
		++id;
	}

	private void firstApiCall() throws IOException, MalformedURLException {
		JSONObject data = urlConnection();
		
		if (data.getJSONObject("feed").has("entry")) {
			
			JSONArray reviews = data.getJSONObject("feed").getJSONArray("entry");
			
			for (int i = 1; i < reviews.length(); ++i) {
				reported.add(reviews.getJSONObject(i).getJSONObject("id").getString("label"));
			}
			
		}
		
	}
	
	private JSONObject urlConnection() throws MalformedURLException, IOException {
		URLConnection connection = new URL(uri + params.getAppId() + uriParams)
				.openConnection();
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
