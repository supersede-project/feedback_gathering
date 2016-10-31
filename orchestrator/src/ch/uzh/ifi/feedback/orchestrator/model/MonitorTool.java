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

import java.util.HashMap;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorToolSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorTypeSerializationService;

@Serialize(MonitorToolSerializationService.class)
public class MonitorTool extends OrchestratorItem<MonitorTool>{

	@Id
	@DbAttribute("monitor_tool_id")
	private Integer id;
	
	@Unique
	@NotNull
	private String name;
	
	@DbAttribute("monitor_type_name")
	private String monitorTypeName;
	
	private String monitor;
	
	public String getMonitorName() {
		return monitor;
	}
	
	public void setMonitorName(String name) {
		this.monitor = name;
	}
	
	public String getMonitorTypeName() {
		return monitorTypeName;
	}

	public void setMonitorTypeName(String monitorTypeName) {
		this.monitorTypeName = monitorTypeName;
	}

	@DbIgnore
	private List<MonitorConfiguration> configurations;
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<MonitorConfiguration> getConfigurations() {
		return configurations;
	}
	
	public void setConfigurations(List<MonitorConfiguration> configurations) {
		this.configurations = configurations;
	}

}
