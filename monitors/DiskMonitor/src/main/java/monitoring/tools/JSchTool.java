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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import monitoring.controller.ToolInterface;
import monitoring.kafka.KafkaCommunication;
import monitoring.model.DiskMonitoringData;
import monitoring.model.DiskMonitoringParams;

public class JSchTool implements ToolInterface<DiskMonitoringParams> {
	
	final static Logger logger = Logger.getLogger(JSchTool.class);
	
	Timer timer;
	
	//Data object instances
	DiskMonitoringParams confParams;
	boolean firstConnection;
	int id = 1;
	int configurationId;
	
	KafkaCommunication kafka;
	
	JSch jsch;
	ChannelExec channel;
	Session session;

	@Override
	public void addConfiguration(DiskMonitoringParams params, int configurationId) throws Exception {
		this.firstConnection = true;
		this.confParams = params;
		this.configurationId = configurationId;
		this.kafka = new KafkaCommunication();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource("ssh");
		jsch = new JSch();
		try {
			jsch.addIdentity(url.toURI().getPath());
		} catch (JSchException | URISyntaxException e) {
			throw new Exception ("Unable to load identity in SSH connection");
		}
		logger.debug("Added private key file");
		
		resetStream();
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		session.disconnect();
		channel.disconnect();
		timer.cancel();
	}
	
	@Override
	public void updateConfiguration(DiskMonitoringParams params) throws Exception {
		deleteConfiguration();
		this.confParams = params;
		resetStream();
	}
	
	private void resetStream() throws Exception {
		//logger.debug("Initialising kafka producer...");
		//kafka.initProducer(confParams.getKafkaEndpoint());
		logger.debug("Initialising proxy...");
		kafka.initProxy();
		logger.debug("Initialising streaming...");
		
		this.firstConnection = true;
		
		try {
			session = jsch.getSession(this.confParams.getUser(), this.confParams.getHost());
		} catch (Exception e) {
			throw new Exception("Unable to start session");
		}
		try {
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		} catch (Exception e) {
			throw new Exception("Unable to connect session");
		}
		logger.debug("SSH session connected...");

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
		BufferedReader in;
		try {
			channel = (ChannelExec) session.openChannel("exec");
			channel.setCommand(this.confParams.getInstruction());
			channel.connect();
			in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
			String msg = null;
			StringBuilder sb = new StringBuilder();
			while( (msg = in.readLine()) !=null){
				if (sb.length() != 0) sb.append("\n");
				sb.append(msg);
			}
			sendData(searchTimeStamp, sb.toString());	
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
	
	private void sendData(String searchTimeStamp, String output) {
		List<DiskMonitoringData> data = new ArrayList<>();
		data.add(new DiskMonitoringData(this.confParams.getLabel(), this.confParams.getInstruction(), output));
		//kafka.generateResponseKafka(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic(), "HttpMonitoredData");
		kafka.generateResponseIF(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic(), "HttpMonitoredData");
		logger.debug("Data successfully sent to Kafka endpoint");
		++id;
	}
	
}
