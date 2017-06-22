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

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import monitoring.controller.ToolInterface;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.HttpMonitoringData;
import monitoring.model.HttpMonitoringParams;

public class ApacheHttp implements ToolInterface<HttpMonitoringParams> {
	
	final static Logger logger = Logger.getLogger(ApacheHttp.class);
	
	Timer timer;
	
	//Data object instances
	HttpMonitoringParams confParams;
	boolean firstConnection;
	int id = 1;
	int configurationId;
	
	KafkaCommunication kafka;
	
	HttpClient client;
    HttpMethod method;
	
	@Override
	public void addConfiguration(HttpMonitoringParams params, int configurationId) throws Exception {
		logger.debug("Adding new configuration");
		this.firstConnection = true;
		this.confParams = params;
		this.configurationId = configurationId;
		this.kafka = new KafkaCommunication();
		
		resetStream();
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}
	
	@Override
	public void updateConfiguration(HttpMonitoringParams params) throws Exception {
		deleteConfiguration();
		//generateData((new Timestamp((new Date()).getTime()).toString()));
		this.confParams = params;
        this.method = new HeadMethod(this.confParams.getUrl());
		resetStream();
	}
	
	private void resetStream() {
		//logger.debug("Initialising kafka producer...");
		//kafka.initProducer(confParams.getKafkaEndpoint());
		logger.debug("Initialising proxy...");
		kafka.initProxy();
		logger.debug("Initialising streaming...");
		
		this.firstConnection = true;
		
		HttpClientParams httpParams = new HttpClientParams();
		httpParams.setConnectionManagerTimeout(30000);
		httpParams.setSoTimeout(30000);
	    this.client = new HttpClient();
	    this.client.setParams(httpParams);
        this.method = new GetMethod(this.confParams.getUrl());
        
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (firstConnection) {
		    		firstConnection = false;
		    	} else {
		    		generateData((new Timestamp((new Date()).getTime()).toString()));
		    	}
			}
		}, 0, Integer.parseInt(confParams.getTimeSlot())* 1000);
		
	}
	
	private void generateData(String searchTimeStamp) {
				
		StopWatch watch = new StopWatch();
		boolean success = true;

        try {
            watch.start();
            client.executeMethod(method);
        } catch (Exception e) {
        	success = false;
        	solveHttpConnection(searchTimeStamp, watch, 404);
        } finally {
        	if (success) {
	        	solveHttpConnection(searchTimeStamp, watch, method.getStatusCode());
        	}
        }
	}
	
	private void solveHttpConnection(String searchTimeStamp, StopWatch watch, int code) {
		watch.stop();
		sendData(searchTimeStamp, watch.getTotalTimeMillis(), code);
		method.releaseConnection();
	}
	
	private void sendData(String searchTimeStamp, long responseTime, int code) {
		List<HttpMonitoringData> data = new ArrayList<>();
		data.add(new HttpMonitoringData(String.valueOf(responseTime), String.valueOf(code)));
		logger.debug("Sent data: " + responseTime + "/" + code);
		//kafka.generateResponseKafka(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic(), "HttpMonitoredData");
		kafka.generateResponseIF(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic(), "HttpMonitoredData");
		logger.debug("Data successfully sent to Kafka endpoint");
		++id;
	}
	
}
