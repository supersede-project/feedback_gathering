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

import monitoring.kafka.KafkaCommunication;
import monitoring.model.MonitoringData;
import monitoring.model.MonitoringParams;
import monitoring.model.Utils;
import monitoring.services.ToolInterface;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
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
		logger.debug("Adding new configuration");
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
			filterQuery.track(Utils.generateKeywordExp(this.confParams.getKeywordExpression()));
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
	
}
