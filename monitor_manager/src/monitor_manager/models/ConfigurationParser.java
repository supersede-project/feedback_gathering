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
package monitor_manager.models;

import com.google.gson.JsonObject;

public class ConfigurationParser {

	public JsonObject getTwitterConfiguration(JsonObject json) {
		JsonObject socialNetworks = new JsonObject();
		JsonObject params = new JsonObject();
		addMainConfigurationParams(json,params);
		if (json.get("keywordEpxression") != null) params.addProperty("keywordExpression", json.get("keywordExpression").getAsString());
		if (json.get("accounts") != null) params.add("accounts", json.get("accounts"));
		
		socialNetworks.add("SocialNetworksMonitoringConfProf", params);
		return socialNetworks;
	}
	
	public JsonObject getGooglePlayConfiguration(JsonObject json) {
		JsonObject googlePlay = new JsonObject();
		JsonObject params = new JsonObject();
		addMainConfigurationParams(json,params);
		params.addProperty("packageName", json.get("packageName").getAsString());
		
		googlePlay.add("GooglePlayConfProf", params);
		return googlePlay;
	}
	
	public JsonObject getAppStoreConfiguration(JsonObject json) {
		JsonObject appStore = new JsonObject();
		JsonObject params = new JsonObject();
		addMainConfigurationParams(json,params);
		params.addProperty("appId", json.get("appId").getAsString());
		
		appStore.add("AppStoreConfProf", params);
		return appStore;
	}
	
	private void addMainConfigurationParams(JsonObject json, JsonObject params) {
		params.addProperty("toolName", json.get("tool").getAsString());
		params.addProperty("timeSlot", json.get("timeSlot").getAsString());
		params.addProperty("kafkaEndpoint", json.get("kafkaEndpoint").getAsString());
		params.addProperty("kafkaTopic", json.get("kafkaTopic").getAsString());
	}
	
}
