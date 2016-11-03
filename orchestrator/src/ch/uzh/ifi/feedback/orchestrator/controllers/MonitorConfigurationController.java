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
package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorToolService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorConfigurationValidator;

@RequestScoped
@Controller(MonitorConfigurationController.class)
public class MonitorConfigurationController extends RestController<MonitorConfiguration> {
	
	private String monitorManagerHost = "localhost:8080/monitor_manager/";
	
	private MonitorToolService monitorToolService;

	public MonitorConfigurationController(
			MonitorConfigurationService dbService,
			MonitorConfigurationValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response,
			MonitorToolService monitorToolService) {
		super(dbService, validator, request, response);
		
		this.monitorToolService = monitorToolService;
	}
	
	@POST
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}")
	public MonitorConfiguration InsertMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			MonitorConfiguration configuration) throws Exception {
		configuration.setMonitorToolName(tool);
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = monitorManagerHost + "configuration";

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(gson.toJson(configuration)).getAsJsonObject();
		json.addProperty("monitor", this.monitorToolService.GetById(tool.hashCode()).getMonitorName());
		HttpPost request = new HttpPost(url);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(json.getAsString()));
		client.execute(request);
		
		return super.Insert(configuration);
	}
	
	@GET
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}/{id-tool-configuration}")
	public MonitorConfiguration GetMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration) throws Exception {
		return super.GetById(configuration.hashCode());
	}
	
	@PUT
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}/{id-tool-configuration}")
	public MonitorConfiguration UpdateMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration,
			MonitorConfiguration monitorConfiguration) throws Exception {
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = monitorManagerHost + "configuration";

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(gson.toJson(configuration)).getAsJsonObject();
		json.addProperty("monitor", this.monitorToolService.GetById(tool.hashCode()).getMonitorName());
		HttpPut request = new HttpPut(url);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(json.getAsString()));
		client.execute(request);
		
		return super.Insert(monitorConfiguration);
	}
	
	@DELETE
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}/{id-tool-configuration}")
	public void DeleteMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration) throws Exception {
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost(monitorManagerHost)
			.setPath("configuration")
		    .setParameter("id", configuration.toString())
		    .setParameter("monitor", super.GetById(configuration).getMonitor());
		URI uri = builder.build();
		HttpDelete request = new HttpDelete(uri);
		client.execute(request);
		
		super.Delete(configuration);
	}

}
