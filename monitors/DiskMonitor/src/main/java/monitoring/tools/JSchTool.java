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
import monitoring.model.Instruction;

public class JSchTool implements ToolInterface<DiskMonitoringParams> {
	
	final static Logger logger = Logger.getLogger(JSchTool.class);
	
	Timer timer;
	
	//Data object instances
	DiskMonitoringParams confParams;
	List<DiskMonitoringData> data;
	boolean firstConnection;
	int id = 1;
	int configurationId;
	
	KafkaCommunication kafka;
	
	//SSH connection tools
	JSch jsch;
	ChannelExec channel;
	Session session;
	
	//localhost connection tools

	@Override
	public void addConfiguration(DiskMonitoringParams params, int configurationId) throws Exception {
		this.firstConnection = true;
		this.confParams = params;
		this.configurationId = configurationId;
		this.kafka = new KafkaCommunication(this.confParams.getKafkaEndpoint());
		this.data = new ArrayList<>();
		if (this.confParams.getHost().equals("localhost")) 
			initLocalhostConnection();
		else
			initSshConnection();
		
	}
	
	@Override
	public void deleteConfiguration() throws Exception {
		this.data = new ArrayList<>();
		if (this.confParams.getHost().equals("localhost")) 
			deleteLocalhostConnection();
		else 
			deleteSshConnection();
	}
	
	@Override
	public void updateConfiguration(DiskMonitoringParams params) throws Exception {
		this.confParams = params;
		this.data = new ArrayList<>();
		if (this.confParams.getHost().equals("localhost")) 
			updateLocalhostConnection();
		else 
			updateSshConnection();
	}
	
	private void initLocalhostConnection() throws Exception {
		resetLocalhostStream();
	}
	
	private void initSshConnection() throws Exception {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			System.out.println(classLoader.getResource(".").getPath());
			URL url = classLoader.getResource("ssh");
			jsch = new JSch();
			jsch.addIdentity(url.toURI().getPath());
		} catch (Exception e) {
			//throw new Exception ("Unable to load identity in SSH connection");
			throw e;
		}
		logger.debug("Added private key file");
		resetSshStream();
	}
	
	private void deleteLocalhostConnection() throws Exception  {
		timer.cancel();
	}
	
	private void deleteSshConnection() throws Exception {
		session.disconnect();
		channel.disconnect();
		timer.cancel();
	}
	
	private void updateLocalhostConnection() throws Exception {
		deleteLocalhostConnection();
		resetLocalhostStream();
	}
	
	private void updateSshConnection() throws Exception {
		deleteSshConnection();
		resetSshStream();
	}
	
	private void resetLocalhostStream() throws Exception {
		logger.debug("Initialising streaming...");
		this.firstConnection = true;
		
		//TODO
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (firstConnection) {
		    		firstConnection = false;
		    	} else {
		    		generateLocalhostData((new Timestamp((new Date()).getTime()).toString()));
		    	}
			}
		}, 0, Integer.parseInt(confParams.getTimeSlot())* 1000);
	}

	private void resetSshStream() throws Exception {
		
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
		    		generateSshData((new Timestamp((new Date()).getTime()).toString()));
		    	}
			}
		}, 0, Integer.parseInt(confParams.getTimeSlot())* 1000);
		
	}
	
	private void generateSshData(String searchTimeStamp) {
		BufferedReader in;
		try {	
			for (int i = 0; i < confParams.getInstructions().size(); ++i) {
				Instruction instruction = confParams.getInstructions().get(i);
				
				channel = (ChannelExec) session.openChannel("exec");
				channel.setCommand(instruction.getInstruction());
				channel.connect();
				
				in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
				String msg = null;
				StringBuilder sb = new StringBuilder();
				while( (msg = in.readLine()) != null){
					if (sb.length() != 0) sb.append("\n");
					sb.append(msg);
				}
				
				data.add(new DiskMonitoringData(instruction.getLabel(), instruction.getInstruction(), sb.toString()));
			}
			
			sendData(searchTimeStamp);	
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
	}
	
	private void generateLocalhostData(String searchTimeStamp) {        
		try {
			
			for (int i = 0; i < confParams.getInstructions().size(); ++i) {
				Instruction instruction = confParams.getInstructions().get(i);
				
				String[] cmd = { "/bin/sh", "-c", instruction.getInstruction() };
				Process proc = Runtime.getRuntime().exec(cmd);
				BufferedReader reader =  
			              new BufferedReader(new InputStreamReader(proc.getInputStream()));
				
				String msg = null;
				StringBuilder sb = new StringBuilder();
		        while( (msg = reader.readLine()) != null) {
		            if (sb.length() != 0) sb.append("\n");
		            sb.append(msg);
		        }
		        proc.waitFor();   
		        data.add(new DiskMonitoringData(instruction.getLabel(), instruction.getInstruction(), sb.toString()));
			
			}
			
			sendData(searchTimeStamp);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	private void sendData(String searchTimeStamp) {
		kafka.sendData(data, searchTimeStamp, id, configurationId, this.confParams.getKafkaTopic(), "DiskMonitoredData");
		logger.debug("Data successfully sent to Kafka endpoint");
		++id;
		data = new ArrayList<>();
	}
	
}
