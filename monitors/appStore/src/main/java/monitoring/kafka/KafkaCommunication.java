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
	
	Producer<String,String> producer;
	KafkaClient proxy;
	
	/**
	 * Creates a new proxy instantiation for IF communication
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
	 * Generates a json formatted response and sends it to the IF
	 */
	public void generateResponseIF(List<MonitoringData> dataList, String timeStamp,
			int outputId, int confId, String topic) {
		JSONObject res = generateData(dataList, timeStamp, outputId, confId);
		proxy.sendMessage(res, topic);
	}

	/**
	 * Generates a json formatted response and sends it to kafka
	 */
	public void generateResponseKafka(List<MonitoringData> dataList, String timeStamp,
			int outputId, int confId, String topic) {
		JSONObject res = generateData(dataList, timeStamp, outputId, confId);
		KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, res.toString());
		producer.send(msg);
		
	}
	
	private JSONObject generateData(List<MonitoringData> dataList, String timeStamp, 
			int outputId, int confId) {
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
			if (data.getReviewText() != null) review.put("reviewText", data.getReviewText());
			if (data.getStarRating() != null) review.put("starRating", data.getStarRating());
			dataItems.put(review);
		}
		JSONObject res = new JSONObject();
		res.put("idOutput", outputId);
		res.put("confId", confId);
		res.put("searchTimeStamp", timeStamp);
		res.put("numDataItems", dataList.size());
		res.put("DataItems", dataItems);
		
		JSONObject fullResponse = new JSONObject();
		fullResponse.put("AppStoreMonitoredData", res);
		return fullResponse;
	}
	
}
