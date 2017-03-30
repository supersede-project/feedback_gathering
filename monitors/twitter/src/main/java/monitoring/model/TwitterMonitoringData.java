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

public class TwitterMonitoringData implements MonitoringData {
	
	private String id;
	private String timeStamp;
	private String message;
	private String author;
	private String link;
	
	public TwitterMonitoringData(String id, String timeStamp, String message, String author, String link) {
		this.id = id;
		this.timeStamp = timeStamp;
		this.message = message;
		this.author = author;
		this.link = link;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public JSONObject toJsonObject() {
		JSONObject json = new JSONObject();
		json.put("id", this.id);
		json.put("timeStamp", this.timeStamp);
		json.put("message", this.message);
		json.put("author", this.author);
		json.put("link", this.link);
		return json;
	}

}
