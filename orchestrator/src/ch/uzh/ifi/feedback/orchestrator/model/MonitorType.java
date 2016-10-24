package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.HashMap;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.orchestrator.serialization.MonitorTypeSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorTypeService;

@Serialize(MonitorTypeSerializationService.class)
public class MonitorType extends OrchestratorItem<MonitorType> {
	
	@Id
	@DbAttribute("monitor_type_id")
	private Integer id;
	
	@Unique
	@NotNull
	private String name;
	
	@DbIgnore
	private List<MonitorTool> tools;
	
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
	
	public List<MonitorTool> getTools() {
		return tools;
	}
	
	public void setTools(List<MonitorTool> tools) {
		this.tools = tools;
	}

}
