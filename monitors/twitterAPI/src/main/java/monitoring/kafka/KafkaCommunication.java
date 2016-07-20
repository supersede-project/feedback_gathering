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
		
		JSONArray items = new JSONArray();
		for (MonitoringData item : dataList) {
			JSONObject jsonItem = new JSONObject();
			
			jsonItem.put("idItem", item.getId());
			jsonItem.put("timeStamp", item.getTimeStamp());
			jsonItem.put("message", item.getMessage());
			jsonItem.put("author", item.getAuthor());
			jsonItem.put("link", item.getLink());
			
			items.put(jsonItem);
		}
		
		JSONObject mainInfo = new JSONObject();
		
		mainInfo.put("idOutput", String.valueOf(id));
		mainInfo.put("searchTimeStamp", timeStamp);
		mainInfo.put("numDataItems", dataList.size());
		mainInfo.put("DataItems", items);
		
		++id;
		
		JSONObject jsonData = new JSONObject();
		
		jsonData.put("SocialNetworksMonitoredData", mainInfo);
		
		KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, jsonData.toString());
		producer.send(msg);
		
	}
	
}
