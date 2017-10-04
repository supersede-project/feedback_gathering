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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
import eu.supersede.integration.api.monitoring.manager.proxies.MonitorManagerProxy;
import eu.supersede.integration.api.monitoring.manager.types.AppStoreMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.GooglePlayMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.HttpMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.MonitorSpecificConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.TwitterMonitorConfiguration;
import javassist.NotFoundException;

@RequestScoped
@Controller(MonitorConfiguration.class)
public class MonitorConfigurationController extends RestController<MonitorConfiguration> {
		
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
		
		MonitorManagerProxy<?, ?> proxy = new MonitorManagerProxy<>();
		MonitorSpecificConfiguration configurationObj = generateMonitorConf(configuration, monitorTool.get(0));
		MonitorSpecificConfiguration createConfiguration = proxy.createMonitorConfiguration(configurationObj);
		configuration.setMonitorManagerConfigurationId(createConfiguration.getId());
		
		MonitorConfiguration conf = super.Insert(configuration);
		conf.setId(configuration.getMonitorManagerConfigurationId());
		conf.setMonitorManagerConfigurationId(null);
		return conf;
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
		List<MonitorConfiguration> monitorConfiguration = this.dbService.GetWhere(Arrays.asList(monitorTool.get(0).getId(), configuration), "monitor_tool_id = ? and monitor_manager_configuration_id = ?");
		if(monitorConfiguration.isEmpty()) {
				throw new NotFoundException("There is no monitor configuration with this id for this monitor tool");
		}
		MonitorConfiguration conf = monitorConfiguration.get(0);
		conf.setId(monitorConfiguration.get(0).getMonitorManagerConfigurationId());
		conf.setMonitorManagerConfigurationId(null);
		return conf;
	}
	
	@PUT
	@Path("/MonitorTypes/{id-type-of-monitor}/Tools/{id-monitoring-tool}/ToolConfigurations/{id-tool-configuration}")
	public MonitorConfiguration UpdateMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
			@PathParam("id-monitoring-tool") String tool,
			@PathParam("id-tool-configuration") Integer configuration,
			MonitorConfiguration monitorConfiguration) throws Exception {
		
		List<MonitorType> monitorType = this.monitorTypeService.GetWhere(Arrays.asList(type), "name = ?");
		if (monitorType.isEmpty()) {
			throw new NotFoundException("There is no monitor type with this name");
		}
		List<MonitorTool> monitorTool = this.monitorToolService.GetWhere(Arrays.asList(monitorType.get(0).getId(), tool), "monitor_type_id = ? and name = ?");
		if(monitorTool.isEmpty()) {
				throw new NotFoundException("There is no monitor tool with this name for this monitor type");
		}
		List<MonitorConfiguration> monitorConf = this.dbService.GetWhere(Arrays.asList(monitorTool.get(0).getId(), configuration), "monitor_tool_id = ? and monitor_manager_configuration_id = ?");
		if(monitorConf.isEmpty()) {
				throw new NotFoundException("There is no monitor configuration with this id for this monitor tool");
		}
		
		monitorConfiguration.setMonitorManagerConfigurationId(configuration);
		monitorConfiguration.setMonitorToolId(monitorTool.get(0).getId());
		monitorConfiguration.setId(monitorConf.get(0).getId());
		MonitorConfiguration conf = super.Update(monitorConfiguration);
		conf.setId(configuration);
		conf.setMonitorManagerConfigurationId(null);
		
		MonitorManagerProxy<?, ?> proxy = new MonitorManagerProxy<>();
		MonitorSpecificConfiguration configurationObj = generateMonitorConf(monitorConfiguration, monitorTool.get(0));
		configurationObj.setId(configuration);
		proxy.updateMonitorConfiguration(configurationObj);
				
		return conf;
	}
	
	@DELETE
	@Path("/MonitorTypes/{id-type-of-monitor}/Tools/{id-monitoring-tool}/ToolConfigurations/{id-tool-configuration}")
	public void DeleteMonitorConfiguration(@PathParam("id-type-of-monitor") String type, 
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
		List<MonitorConfiguration> monitorConf = this.dbService.GetWhere(Arrays.asList(monitorTool.get(0).getId(), configuration), "monitor_tool_id = ? and monitor_manager_configuration_id = ?");
		if(monitorConf.isEmpty()) {
				throw new NotFoundException("There is no monitor configuration with this id for this monitor tool");
		}

		super.Delete(monitorConf.get(0).getId());
		
		MonitorManagerProxy<?, ?> proxy = new MonitorManagerProxy<>();
		MonitorSpecificConfiguration deleteConf = generateMonitorConf(monitorConf.get(0), monitorTool.get(0));
		deleteConf.setId(configuration);
		proxy.deleteMonitorConfiguration(deleteConf);
		
	}
	
	private MonitorSpecificConfiguration generateMonitorConf(MonitorConfiguration configuration, MonitorTool tool) throws Exception {
		MonitorSpecificConfiguration monitorManagerConf = null;
		if (tool.getMonitorName().equals("Twitter")) {
			monitorManagerConf = new TwitterMonitorConfiguration();
			((TwitterMonitorConfiguration) monitorManagerConf).setKeywordExpression(configuration.getKeywordExpression());
		}
		else if (tool.getMonitorName().equals("GooglePlay")) {
			monitorManagerConf = new GooglePlayMonitorConfiguration();
			((GooglePlayMonitorConfiguration) monitorManagerConf).setPackageName(configuration.getPackageName());
		}
		else if (tool.getMonitorName().equals("AppStore")) {
			monitorManagerConf = new AppStoreMonitorConfiguration();
			((AppStoreMonitorConfiguration) monitorManagerConf).setAppId(configuration.getAppId());
		}
		else if (tool.getMonitorName().equals("Http")) {
			monitorManagerConf = new HttpMonitorConfiguration();
			((HttpMonitorConfiguration) monitorManagerConf).setUrl(configuration.getUrl());
		}
		monitorManagerConf.setKafkaEndpoint(new URL(configuration.getKafkaEndpoint()));
		monitorManagerConf.setKafkaTopic(configuration.getKafkaTopic());
		monitorManagerConf.setTimeSlot(Integer.parseInt(configuration.getTimeSlot()));
		monitorManagerConf.setToolName(tool.getName());
		return monitorManagerConf;
	}

}
