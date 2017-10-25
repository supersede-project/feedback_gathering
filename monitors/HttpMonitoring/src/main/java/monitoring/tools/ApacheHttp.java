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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
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
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import monitoring.controller.ToolInterface;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.HttpMonitoringData;
import monitoring.model.HttpMonitoringParams;
import monitoring.model.Method;

public class ApacheHttp implements ToolInterface<HttpMonitoringParams> {
	
	final static Logger logger = Logger.getLogger(ApacheHttp.class);
	
	Timer timer;
	
	//Data object instances
	HttpMonitoringParams confParams;
	boolean firstConnection;
	int id = 0;
	int configurationId;
	
	KafkaCommunication kafka;
		
	@Override
	public void addConfiguration(HttpMonitoringParams params, int configurationId) throws Exception {
		logger.debug("Adding new configuration");
		this.confParams = params;
		this.configurationId = configurationId;
		this.kafka = new KafkaCommunication(this.confParams.getKafkaEndpoint());
		resetStream();
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		timer.cancel();
	}
	
	@Override
	public void updateConfiguration(HttpMonitoringParams params) throws Exception {
		deleteConfiguration();
		this.confParams = params;
		this.kafka = new KafkaCommunication(this.confParams.getKafkaEndpoint());
		resetStream();
	}
	
	Part[] parts;
	
	private void resetStream() throws Exception {
		logger.debug("Initialising streaming...");
		
		this.firstConnection = true;
		
		if (this.confParams.getFile() != null) {
			try {
				Part[] parts = {
		    			new StringPart("json", this.confParams.getBody().toString()),
		    			new FilePart(this.confParams.getFile().getName(), convert(this.confParams.getFile()))
		    	};
				this.parts = parts;
			} catch (Exception e) {
				
			}
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (firstConnection) {
		    		firstConnection = false;
		    	} else {
		    		MyThread thread = new MyThread();
		    		thread.start();
		    	}
			}
		}, 0, Integer.parseInt(confParams.getTimeSlot()));
		
	}

	public File convert(MultipartFile file) throws Exception {    
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
	
	private void sendData(String searchTimeStamp, long responseTime, int code) {
		List<HttpMonitoringData> data = new ArrayList<>();
		data.add(new HttpMonitoringData(String.valueOf(responseTime), String.valueOf(code), confParams.getTimeSlot()));
		logger.debug("Sent data: " + responseTime + "/" + code);
		kafka.sendData(data, searchTimeStamp, ++this.id, configurationId, confParams.getKafkaTopic(), "HttpMonitoredData");
		logger.debug("Data successfully sent to Kafka endpoint");
	}
	
	public class MyThread extends Thread {
		
		HttpClient client;
		HttpMethod method;
		
		@Override
		public void run() {
			HttpClientParams httpParams = new HttpClientParams();
			httpParams.setConnectionManagerTimeout(30000);
			httpParams.setSoTimeout(30000);
			
		    this.client = new HttpClient();
		    this.client.setParams(httpParams);
		    
	        if (confParams.getMethod().equals(Method.GET)) 
	        	initGetMethod();
	        else if (confParams.getMethod().equals(Method.POST)) {
				try {
					initPostMethod();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
    		generateData((new Timestamp((new Date()).getTime()).toString()));
		}
		
		private void generateData(String searchTimeStamp) {
			
			StopWatch watch = new StopWatch();
			boolean success = true;

	        try {
	            watch.start();
	            client.executeMethod(method);
	        } catch (Exception e) {
	        	success = false;
	        	System.out.println(e);
	        	solveHttpConnection(searchTimeStamp, watch, 500);
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
		
		private void initPostMethod() throws UnsupportedEncodingException {
			PostMethod postMethod = new PostMethod(confParams.getUrl());
	    	for (String key : confParams.getHeaders().keySet()) {
	    		postMethod.setRequestHeader(key, confParams.getHeaders().get(key));
	    	}
	    	if (confParams.getFile() != null) {
		    	postMethod.setRequestEntity(
		    			new MultipartRequestEntity(parts, postMethod.getParams())
		    			);
	    	} else {
	    		postMethod.setRequestEntity(
	    				new StringRequestEntity(confParams.getBody().toString(), 
	    				"application/json",
	    			    "UTF-8"));
	    	}
	    	method = postMethod;
		}

		private void initGetMethod() {
			method = new GetMethod(confParams.getUrl());
		}
		
	}
	
}
