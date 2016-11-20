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
package ch.uzh.ifi.feedback.orchestrator.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorConfigurationSerializationService;

@Serialize(MonitorConfigurationSerializationService.class)
public class MonitorConfiguration extends OrchestratorItem<MonitorConfiguration> {

	@Id
	@DbAttribute("monitor_configurations_id")
	private Integer id;
	
	@DbAttribute("monitor_tool_name")
	private String monitorToolName;
	
	@DbAttribute("monitor_tool_monitor")
	private String monitor;
	
	private String configSender;

	private String timeStamp;
	
	//Configuration candidates, only one of them is deserialized
	private AppStoreConfiguration AppStoreConfiguration;
	private GooglePlayConfiguration GooglePlayConfiguration;
	private SocialNetworksMonitorConfiguration SocialNetworksMonitorConfiguration;
	
	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public String getMonitorToolName() {
		return monitorToolName;
	}

	public void setMonitorToolName(String monitorToolName) {
		this.monitorToolName = monitorToolName;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getConfigSender() {
		return configSender;
	}

	public void setConfigSender(String configSender) {
		this.configSender = configSender;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public AppStoreConfiguration getAppStoreConfiguration() {
		return AppStoreConfiguration;
	}

	public void setAppStoreConfiguration(AppStoreConfiguration appStoreConfiguration) {
		AppStoreConfiguration = appStoreConfiguration;
	}

	public GooglePlayConfiguration getGooglePlayConfiguration() {
		return GooglePlayConfiguration;
	}

	public void setGooglePlayConfiguration(GooglePlayConfiguration googlePlayConfiguration) {
		GooglePlayConfiguration = googlePlayConfiguration;
	}

	public SocialNetworksMonitorConfiguration getSocialNetworksMonitorConfiguration() {
		return SocialNetworksMonitorConfiguration;
	}

	public void setSocialNetworksMonitorConfiguration(
			SocialNetworksMonitorConfiguration socialNetworksMonitorConfiguration) {
		SocialNetworksMonitorConfiguration = socialNetworksMonitorConfiguration;
	}

}
