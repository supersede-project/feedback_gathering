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
package monitoring.model;

import org.json.JSONObject;

import monitoring.model.MonitoringData;

public class HttpMonitoringData implements MonitoringData {
	
	private String responseTime;
	private String responseCode;
	
	public HttpMonitoringData(String responseTime, String responseCode) {
		this.responseTime = responseTime;
		this.responseCode = responseCode;
	}
	
	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public JSONObject toJsonObject() {
		JSONObject json = new JSONObject();
		json.put("responseTime", this.responseTime);
		json.put("responseCode", this.responseCode);
		return json;
	}

}
