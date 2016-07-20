package monitoring.kafka;

import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import monitoring.model.MonitoringData;

public class KafkaCommunication {
	
	/**
	 * Creates and returns a new producer for kafka communication purposes
	 * @param kafkaEndpoint		the endpoint of the producer
	 * @return					the created producer
	 */
	public static Producer<String, String> initProducer(String kafkaEndpoint) {
		Properties props = new Properties();
		props.put("metadata.broker.list", kafkaEndpoint);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		
		return new Producer<String,String>(config);
	}
	
	/**
	 * Generates a json formatted response and sends it to kafka
	 * @param dataList			a list with the data to retrieve
	 * @param timeStamp			the timestamp of the search
	 * @param producer			the producer to communicate 
	 * @param id				the id of the response data
	 * @param topic				the topic for the kafka communication
	 */
	public static void generateResponse(List<MonitoringData> dataList, String timeStamp, 
		Producer<String, String> producer, int id, String topic) {
		
		JSONArray dataItems = new JSONArray();
		
		for (MonitoringData data : dataList) {
			JSONObject review = new JSONObject();
			
			if (data.getAppVersion() != null) review.put("appVersion", data.getAppVersion());
			if (data.getAuthorName() != null) review.put("authorName", data.getAuthorName());
			if (data.getTimeStamp() != null) review.put("timeStamp", data.getTimeStamp());
			if (data.getDevice() != null) review.put("device", data.getDevice());
			if (data.getLink() != null) review.put("link", data.getLink());
			if (data.getReviewerLanguage() != null) review.put("reviewerLanguage", data.getReviewerLanguage());
			if (data.getReviewID() != null) review.put("reviewID", data.getReviewID());
			if (data.getReviewTitle() != null) review.put("reviewTitle", data.getReviewTitle());
			if (data.getReviewText() != null) review.put("reviewText", data.getReviewText());
			if (data.getStarRating() != null) review.put("starRating", data.getStarRating());
			
			dataItems.put(review);
		}
		
		JSONObject res = new JSONObject();
		res.put("idOutput", id);
		res.put("searchTimeStamp", timeStamp);
		res.put("numDataItems", dataList.size());
		res.put("DataItems", dataItems);
		
		JSONObject fullResponse = new JSONObject();
		fullResponse.put("GooglePlayMonitoredData", res);
		
		KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, res.toString());
		producer.send(msg);
		
	}
	
}
