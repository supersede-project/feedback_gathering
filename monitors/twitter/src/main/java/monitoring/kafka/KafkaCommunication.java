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
package monitoring.kafka;

import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.supersede.integration.api.analysis.proxies.KafkaClient;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import monitoring.model.MonitoringData;

public class KafkaCommunication {
	
	private Producer<String,String> producer;
	private KafkaClient proxy;
	
	/**
	 * Generates a json formatted response and sends it to the IF Kafka consumer
	 */
	public void generateResponseIF(List<MonitoringData> dataList, String timeStamp, 
		int outputId, int confId, String topic) {
		JSONObject jsonData = generateData(dataList, timeStamp, outputId, confId);
		proxy.sendMessage(jsonData, topic);
	}
	
	/**
	 * Generates a json formatted response and sends it to a custom kafka consumer
	 */
	public void generateResponseKafka(List<MonitoringData> dataList, String timeStamp,
			int outputId, int confId, String topic) {
		JSONObject jsonData = generateData(dataList, timeStamp, outputId, confId);
		KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, jsonData.toString());
		producer.send(msg);
	}
	
	/**
	 * Creates a new proxy for IF kafka communication
	 */
	public void initProxy(String kafkaEndpoint) {
		proxy = new KafkaClient(kafkaEndpoint);
	}
	
	/**
	 * Creates a new producer for kafka communication
	 */
	public void initProducer(String kafkaEndpoint) {
		Properties props = new Properties();
		props.put("metadata.broker.list", kafkaEndpoint);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		this.producer = new Producer<String,String>(config);
	}
	
	/**
	 * Generates the data object to be retrieved
	 */
	private JSONObject generateData(List<MonitoringData> dataList, String timeStamp, int outputId, int confId) {
		
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
		mainInfo.put("idOutput", String.valueOf(outputId));
		mainInfo.put("confId", String.valueOf(confId));
		mainInfo.put("searchTimeStamp", timeStamp);
		mainInfo.put("numDataItems", dataList.size());
		mainInfo.put("DataItems", items);
		++outputId;
		
		JSONObject jsonData = new JSONObject();
		jsonData.put("SocialNetworksMonitoredData", mainInfo);
		
		return jsonData;
		
	}
	
}
