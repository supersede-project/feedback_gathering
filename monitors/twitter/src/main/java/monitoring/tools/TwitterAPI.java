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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import eu.supersede.integration.api.analysis.proxies.KafkaClient;
import kafka.javaapi.producer.Producer;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringData;
import monitoring.model.MonitoringParams;
import monitoring.services.ToolInterface;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;

public class TwitterAPI implements ToolInterface {
	
	final static Logger logger = Logger.getLogger(TwitterAPI.class);
	
	//Streaming object instances
	TwitterStream stream;
	List<Status> tweetInfo;
	Timer timer;
	
	//Data object instances
	MonitoringParams confParams;
	boolean firstConnection;
	int id = 1;
	int configurationId;
	
	KafkaCommunication kafka;
	
	@Override
	public void addConfiguration(MonitoringParams params, int configurationId) {
		this.confParams = params;
		this.configurationId = configurationId;
		this.kafka = new KafkaCommunication();
		resetStream();
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
		stream.cleanUp();
		stream.shutdown();
	}
	
	@Override
	public void updateConfiguration(MonitoringParams params) throws Exception {
		deleteConfiguration();
		generateData((new Timestamp((new Date()).getTime()).toString()));
		this.confParams = params;
		resetStream();
	}
	
	private void resetStream() {
		//logger.debug("Initialising kafka producer...");
		//kafka.initProducer(confParams.getKafkaEndpoint());
		logger.debug("Initialising proxy...");
		kafka.initProxy(confParams.getKafkaEndpoint());
		logger.debug("Initialising streaming...");
		firstConnection = true;
		tweetInfo = new ArrayList<>();
		stream = new TwitterStreamFactory().getInstance();
		stream.onStatus(new Consumer<Status>() {
			@Override
			public void accept(Status arg0) {
				if (confParams.getAccounts() != null && !confParams.getAccounts().isEmpty()) {
					if (confParams.getAccounts().contains(arg0.getUser().getScreenName())) { 
						tweetInfo.add(arg0);
					}
				}
				else tweetInfo.add(arg0);
			}
		});
		stream.addConnectionLifeCycleListener(new ConnectionLifeCycleListener() {
			@Override
			public void onCleanUp() {
			}
			@Override
			public void onConnect() {
				logger.debug("Connection established successfully");
				timer = new Timer();
				timer.schedule(new TimerTask() {
				    public void run() {
				    	if (firstConnection) {
				    		firstConnection = false;
				    	} else {
				    		generateData((new Timestamp((new Date()).getTime()).toString()));
				    	}
				    }
				}, 0, Integer.parseInt(confParams.getTimeSlot())* 1000);
			}
			@Override
			public void onDisconnect() {
				logger.debug("Connection closed");
			}
		});
		
		FilterQuery filterQuery = new FilterQuery();
		if (this.confParams.getKeywordExpression() != null) 
			filterQuery.track(generateKeywordExp(this.confParams.getKeywordExpression()));
		stream.filter(filterQuery);
	}
	
	private void generateData(String searchTimeStamp) {
		
		List<MonitoringData> data = new ArrayList<>();
		for (Status s : tweetInfo) {
			String id = String.valueOf(s.getId());
			String timeStamp = String.valueOf(s.getCreatedAt());
			String message = s.getText();
			String author = "@" + s.getUser().getScreenName();
			String link = "https://twitter.com/" + s.getUser().getName().replace(" ", "")+ "/status/" + s.getId();
			MonitoringData dataObj = new MonitoringData(id, timeStamp, message, author, link);
			data.add(dataObj);
		}
		tweetInfo = new ArrayList<>();
		
		//kafka.generateResponse(data, searchTimeStamp, this.producer, id, configurationId, this.confParams.getKafkaTopic);
		kafka.generateResponse(data, searchTimeStamp, id, configurationId);
		logger.debug("Data successfully sent to Kafka endpoint");
		++id;
	}

	private String[] generateKeywordExp(String keywordExpression) {
		String[] blocks = keywordExpression.split(" AND ");
		for (int i = 0; i < blocks.length; ++i) {
			blocks[i] = blocks[i].replace("(", "");
			blocks[i] = blocks[i].replace(")", "");
		}
		List<List<String>> cnfCombinations = new ArrayList<>();
		for (int i = 0; i < blocks.length; ++i) 
			cnfCombinations.add(getKeyElements(blocks[i]));
		List<List<String>> dnfCombinations = new ArrayList<>();
		for (int i = 0; i < cnfCombinations.get(0).size(); ++i) 
			dnfCombinations.addAll(getDnfCombination(cnfCombinations, 0, i));
		
		String[] keywordDNFExpression = new String[dnfCombinations.size()];
		for (int i = 0; i < dnfCombinations.size(); ++i) {
			String keyword = "";
			for (String s : dnfCombinations.get(i)) 
				keyword += s + " ";
			keywordDNFExpression[i] = keyword;
		}
		
		return keywordDNFExpression;
	}
	
	/**
	 * Backtracking method for getting all combinations in DNF
	 */
	private List<List<String>> getDnfCombination(List<List<String>> cnfCombination, int k, int j) {
		List<List<String>> dnf = new ArrayList<>();
		if (k == cnfCombination.size() -1 ) {
			List<String> l = new ArrayList<>();
			l.add(cnfCombination.get(k).get(j));
			dnf.add(l);
		}
		else {
			for (int i = 0; i < cnfCombination.get(k+1).size(); ++i) {
				List<List<String>> lists = getDnfCombination(cnfCombination, k + 1, i);
				for (List<String> l : lists) l.add(cnfCombination.get(k).get(j));
				dnf.addAll(lists);
			}
		}
		return dnf;
	}
	
	private List<String> getKeyElements(String block) {
		String[] elements = block.split(" OR ");
		List<String> keyElements = new ArrayList<>();
		for (int i = 0; i < elements.length; ++i) {
			keyElements.add(elements[i]);
		}
		return keyElements;
	}
	
}
