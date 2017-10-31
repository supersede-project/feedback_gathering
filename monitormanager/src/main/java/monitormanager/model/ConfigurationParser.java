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
package monitormanager.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import eu.supersede.integration.api.monitoring.manager.types.AppStoreMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.GooglePlayMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.HttpMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.Method;
import eu.supersede.integration.api.monitoring.manager.types.MonitorSpecificConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.TwitterMonitorConfiguration;

public class ConfigurationParser {

	public TwitterMonitorConfiguration getTwitterConfiguration(JsonObject json) throws Exception {
		JsonObject in = json.getAsJsonObject("SocialNetworks");
		TwitterMonitorConfiguration configuration = new TwitterMonitorConfiguration();
		setMonitorConfigurationParams(configuration, in);
		if (in.has("keywordExpression")) configuration.setKeywordExpression(in.get("keywordExpression").getAsString());
		if (in.has("accounts")) {
			ArrayList<String> listdata = new ArrayList<>();     
			JsonArray jArray = in.get("accounts").getAsJsonArray(); 
			if (jArray != null) { 
			   for (int i=0; i < jArray.size(); ++i){ 
				   listdata.add(jArray.get(i).getAsString());
			   } 
			} 
			configuration.setAccounts(listdata);
		}
		
		return configuration;
	}
	
	public GooglePlayMonitorConfiguration getGooglePlayConfiguration(JsonObject json) throws Exception {
		JsonObject in = json.getAsJsonObject("MarketPlaces");
		GooglePlayMonitorConfiguration configuration = new GooglePlayMonitorConfiguration();
		setMonitorConfigurationParams(configuration, in);
		if (in.has("packageName")) configuration.setPackageName(in.get("packageName").getAsString());
		
		return configuration;
	}
	
	public AppStoreMonitorConfiguration getAppStoreConfiguration(JsonObject json) throws Exception {
		JsonObject in = json.getAsJsonObject("MarketPlaces");
		AppStoreMonitorConfiguration configuration = new AppStoreMonitorConfiguration();
		setMonitorConfigurationParams(configuration, in);
		if (in.has("appId")) configuration.setAppId(in.get("appId").getAsString());
		
		return configuration;
	}
	
	public HttpMonitorConfiguration getHttpConfiguration(JsonObject json) throws Exception {
		JsonObject in = json.getAsJsonObject("QoS");
		HttpMonitorConfiguration configuration = new HttpMonitorConfiguration();
		setMonitorConfigurationParams(configuration, in);
		if (in.has("url")) configuration.setUrl(in.get("url").getAsString());
		if (in.has("method")) configuration.setMethod(Method.valueOf(in.get("method").getAsString()));
		
		return configuration;
	}
	
	private void setMonitorConfigurationParams(MonitorSpecificConfiguration configuration, JsonObject in) throws Exception {
		if (in.has("kafkaEndpoint"))
			try {
				configuration.setKafkaEndpoint(new URL(in.get("kafkaEndpoint").getAsString()));
			} catch (MalformedURLException e) {
				throw new Exception("Invalid kafkaEndpoint");
			}
		if (in.has("kafkaTopic")) configuration.setKafkaTopic(in.get("kafkaTopic").getAsString());
		if (in.has("timeSlot")) configuration.setTimeSlot(in.get("timeSlot").getAsInt());
		if (in.has("toolName")) {
			configuration.setToolName(in.get("toolName").getAsString());
		}
	}
	
}
