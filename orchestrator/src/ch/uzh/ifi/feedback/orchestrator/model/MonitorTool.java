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
