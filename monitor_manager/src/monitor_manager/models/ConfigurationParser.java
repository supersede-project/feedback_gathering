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
		return json.getAsJsonObject("SocialNetworksMonitorConfiguration");
	}
	
	public JsonObject getGooglePlayConfiguration(JsonObject json) {
		return json.getAsJsonObject("GooglePlayConfiguration");
	}
	
	public JsonObject getAppStoreConfiguration(JsonObject json) {
		return json.getAsJsonObject("AppStoreConfiguration");
	}
	
}
