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
package ch.uzh.ifi.feedback.orchestrator.monitoring.controllers;

import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
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
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorType;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorToolService;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorTypeService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorConfigurationValidator;
import javassist.NotFoundException;

@RequestScoped
@Controller(MonitorConfiguration.class)
public class MonitorConfigurationController extends RestController<MonitorConfiguration> {
	
	private String monitorManagerHost = "localhost:8080/monitor_manager/";
	
	private MonitorTypeService monitorTypeService;
	private MonitorToolService monitorToolService;

	@Inject
	public MonitorConfigurationController(
			MonitorConfigurationService dbService,
			MonitorConfigurationValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response,
			MonitorToolService monitorToolService,
			MonitorTypeService monitorTypeService) {
		super(dbService, validator, request, response);
		
		this.monitorTypeService = monitorTypeService;
		this.monitorToolService = monitorToolService;
	}
	
	@POST
	@Path("/MonitorTypes/{id-type-of-monitor}/Tools/{id-monitoring-tool}/ToolConfigurations")
	public MonitorConfiguration InsertMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			MonitorConfiguration configuration) throws Exception {
		List<MonitorType> monitorType = this.monitorTypeService.GetWhere(Arrays.asList(type), "name = ?");
		if (monitorType.isEmpty()) {
			throw new NotFoundException("There is no monitor type with this name");
		}
		List<MonitorTool> monitorTool = this.monitorToolService.GetWhere(Arrays.asList(monitorType.get(0).getId(), tool), "monitor_type_id = ? and name = ?");
		if(monitorTool.isEmpty()) {
				throw new NotFoundException("There is no monitor tool with this name for this monitor type");
		}
		configuration.setMonitorToolId(monitorTool.get(0).getId());
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = monitorManagerHost + "configuration";
		
		JsonObject json = getJson(configuration);
				
		json.addProperty("monitor", monitorTool.get(0).getMonitorName());
		HttpPost request = new HttpPost(url);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(json.toString()));
		try {
			client.execute(request);
		} catch (Exception e) {
			//throw new NotFoundException("There was a connection problem with the Monitor Manager");
		}
		
		return super.Insert(configuration);
	}
	
	@GET
	@Path("/MonitorTypes/{id-type-of-monitor}/Tools/{id-monitoring-tool}/ToolConfigurations/{id-tool-configuration}")
	public MonitorConfiguration GetMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration) throws Exception {
		List<MonitorType> monitorType = this.monitorTypeService.GetWhere(Arrays.asList(type), "name = ?");
		if (monitorType.isEmpty()) {
			throw new NotFoundException("There is no monitor type with this name");
		}
		List<MonitorTool> monitorTool = this.monitorToolService.GetWhere(Arrays.asList(monitorType.get(0).getId(), tool), "monitor_type_id = ? and name = ?");
		if(monitorTool.isEmpty()) {
				throw new NotFoundException("There is no monitor tool with this name for this monitor type");
		}
		List<MonitorConfiguration> monitorConfiguration = this.dbService.GetWhere(Arrays.asList(monitorTool.get(0).getId(), configuration), "monitor_tool_id = ? and monitor_configuration_id = ?");
		if(monitorConfiguration.isEmpty()) {
				throw new NotFoundException("There is no monitor configuration with this id for this monitor tool");
		}
		return monitorConfiguration.get(0);
	}
	
	@PUT
	@Path("/MonitorTypes/{id-type-of-monitor}/Tools/{id-monitoring-tool}/ToolConfigurations/{id-tool-configuration}")
	public MonitorConfiguration UpdateMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration,
			MonitorConfiguration monitorConfiguration) throws Exception {
		
		MonitorConfiguration oldConfig = dbService.GetById(configuration);
		if(!oldConfig.getId().equals(configuration))
			throw new NotFoundException("The monitor configuration does not exist");
		
		List<MonitorTool> monitorTools = monitorToolService.GetWhere(Arrays.asList(tool), "name = ?");

		monitorConfiguration.setId(configuration);
		monitorConfiguration.setMonitorToolId(monitorTools.get(0).getId());
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String url = monitorManagerHost + "configuration";

		JsonObject json = getJson(monitorConfiguration);
		
		List<MonitorTool> monitorTool = monitorToolService.GetWhere(Arrays.asList(tool), "name = ?");
		monitorConfiguration.setMonitorToolId(monitorTool.get(0).getId());
		json.addProperty("monitor", monitorTool.get(0).getMonitorName());
		HttpPut request = new HttpPut(url);
		request.addHeader("content-type", "application/json");
		request.setEntity(new StringEntity(json.toString()));
		
		try {
			client.execute(request);
		} catch (Exception e) {
			//throw new NotFoundException("There was a connection problem with the Monitor Manager");
		}
		
		return super.Update(monitorConfiguration);
	}
	
	@DELETE
	@Path("/MonitorTypes/{id-type-of-monitor}/Tools/{id-monitoring-tool}/ToolConfigurations/{id-tool-configuration}")
	public void DeleteMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration) throws Exception {
		
		CloseableHttpClient client = HttpClientBuilder.create().build();
		URIBuilder builder = new URIBuilder();
		
		List<MonitorTool> monitorTool = monitorToolService.GetWhere(Arrays.asList(tool), "name = ?");
		
		builder.setScheme("http").setHost(monitorManagerHost)
			.setPath("configuration")
		    .setParameter("id", configuration.toString())
		    .setParameter("monitor", monitorTool.get(0).getMonitorName());
		URI uri = builder.build();
		HttpDelete request = new HttpDelete(uri);
		
		try {
			client.execute(request);
		} catch (Exception e) {
			//throw new NotFoundException("There was a connection problem with the Monitor Manager");
		}
		
		super.Delete(configuration);
	}
	
	private JsonObject getJson(MonitorConfiguration configuration) throws SQLException, NotFoundException {
		
		MonitorTool tool = monitorToolService.GetById(configuration.getMonitorToolId());
		
		JsonObject json = new JsonObject();
		
		json.addProperty("id", configuration.getId());
		json.addProperty("kafkaEndpoint", configuration.getKafkaEndpoint());
		json.addProperty("kafkaTopic", configuration.getKafkaTopic());
		json.addProperty("toolName",tool.getName());
		json.addProperty("monitor", tool.getMonitorName());
		json.addProperty("timeSlot", configuration.getTimeSlot());
		json.addProperty("timeStamp", configuration.getTimeStamp());
		if (configuration.getAppId() != null) {
			json.addProperty("appId", configuration.getAppId());
		}
		if (configuration.getPackageName() != null) {
			json.addProperty("packageName", configuration.getPackageName());
		}
		if (configuration.getKeywordExpression() != null) {
			json.addProperty("keywordExpression", configuration.getKeywordExpression());
		}
		/*if (configuration.getAccounts() != null) {
		 * json.addProperty("accounts", configuration.getAccounts());
		 */
		
		JsonObject conf = new JsonObject();
		if (configuration.getAppId() != null || configuration.getPackageName() != null)
			conf.add("MarketPlaces", json);
		else if (configuration.getKeywordExpression() != null) 
			conf.add("SocialNetworks", json);
		return conf;
	}

}
