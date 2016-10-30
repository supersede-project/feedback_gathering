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
package monitor_manager.controllers;

import java.io.IOException;
import java.net.URI;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import monitor_manager.models.ConfigurationParser;

@Singleton
public class MonitorManagerController {
	
	private String monitorHost = "localhost:8080";
	
	private ConfigurationParser parser = new ConfigurationParser();

	@POST
	@Path("configuration")
	public void addConfiguration(String json) throws Exception {
		JsonObject jsonObj = getJson(json);
		
		JsonObject configuration = null;
		
		switch (jsonObj.get("monitor").getAsString()) {
			case "TwitterAPI":
				configuration = parser.getTwitterConfiguration(jsonObj);
				break;
			case "GooglePlay":
				configuration = parser.getGooglePlayConfiguration(jsonObj);
				break;
			case "AppStore":
				configuration = parser.getAppStoreConfiguration(jsonObj);
				break;
		}
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(monitorHost)
			.setPath("/configuration")
		    .setParameter("configuration", configuration.toString());
		URI uri = builder.build();
		HttpPost httppost = new HttpPost(uri);
		client.execute(httppost);
	}
	
	@PUT
	@Path("configuration")
	public void updateConfiguration(String json) {
		JsonObject jsonObj = getJson(json);
		//TODO still not implemented in monitor's side
		//CloseableHttpClient client = HttpClientBuilder.create().build();
		//String url = monitorManagerHost + "updateConfiguration";
		//HttpPut request = new HttpPut(url);
		//request.addHeader("content-type", "application/json");
		//request.setEntity(new StringEntity(getConfigurationJson(monitorConfiguration).toString()));
		//client.execute(request);
	}
	
	@DELETE
	@Path("configuration")
	public void deleteConfiguration(@QueryParam("id") String id, @QueryParam("monitor") String monitor) 
			throws Exception {
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = monitorHost + 
				"/" + monitor + 
				"/configuration/" + id;
		HttpDelete request = new HttpDelete(url);
		client.execute(request);
	}
	
	private JsonObject getJson(String configuration) {
		JsonParser jsonParser = new JsonParser();
		JsonObject json = (JsonObject)jsonParser.parse(configuration);
		return json;
	}
	
}
