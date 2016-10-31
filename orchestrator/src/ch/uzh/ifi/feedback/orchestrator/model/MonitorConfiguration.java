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

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorTypeSerializationService;

@Serialize(MonitorConfigurationSerializationService.class)
public class MonitorConfiguration extends OrchestratorItem<MonitorConfiguration> {

	@Id
	@DbAttribute("monitor_configuration_id")
	private Integer id;
	
	@DbAttribute("monitor_tool_name")
	private String monitorToolName;
	
	@DbAttribute("monitor_tool_monitor")
	private String monitor;
	
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

	@NotNull
	private String kafkaEndpoint;
	
	@NotNull
	private String kafkaTopic;
	
	@NotNull
	private String timeSlot;
	
	private HashMap<String,String> params;

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKafkaEndpoint() {
		return kafkaEndpoint;
	}

	public void setKafkaEndpoint(String kafkaEndpoint) {
		this.kafkaEndpoint = kafkaEndpoint;
	}
	
	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}
	
	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	public String getParam(String key) {
		return params.get(key);
	}
	
	public HashMap<String, String> getParams() {
		return params;
	}
	
	public void setParam(String key, String value) {
		params.put(key, value);
	}

}
