package kafkaProducer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


 public class KafkaProducer {
    private static Producer<Integer, String> producer;
    private static String topic = "test"; //Default
    private static String endpoint = "localhost:9092"; //Default
    private JSONObject jsonOutput;

    private void initialize() {
	  //Set the configuration of the KafkaServer
      Properties producerProps = new Properties();
      producerProps.put("metadata.broker.list", endpoint);
      producerProps.put("serializer.class", "kafka.serializer.StringEncoder");
      producerProps.put("request.required.acks", "1");
      ProducerConfig producerConfig = new ProducerConfig(producerProps);
      producer = new Producer<Integer, String>(producerConfig);
    }
    private void publishMesssage() throws Exception{

        KeyedMessage<Integer, String> keyedMsg =
                     new KeyedMessage<Integer, String>(topic, jsonOutput.toJSONString());
        producer.send(keyedMsg); // This publishes message on given topic
    }
    private void createJSON(String timeStamp, String numItem, String metrics){
    	jsonOutput = new JSONObject();
    	jsonOutput.put("timeStamp", timeStamp);
    	jsonOutput.put("numItems", numItem);
    	JSONArray metricArray = new JSONArray();
    	String[] metricsSplits = metrics.split(",,");
    	for (int i = 0; i < metricsSplits.length; i++)
    	{
    		JSONArray metricUnit = new JSONArray();
    		String[] metricUnitSplit = metricsSplits[i].split(","); 
    		for(int j = 0; j< metricUnitSplit.length; j++)
    			metricUnit.add(metricUnitSplit[j]);
    		metricArray.add(metricUnit);
    	}
    	jsonOutput.put("metrics", metricArray);
    }
    public static void main(String[] args) throws Exception {
          KafkaProducer kafkaProducer = new KafkaProducer();
          // Initialize producer
          topic=args[1];
          endpoint=args[0];
          kafkaProducer.initialize();
          // Publish message
          kafkaProducer.createJSON(args[2], args[3], args[4]);
          kafkaProducer.publishMesssage();
          //Close the producer
          producer.close();
    }
}