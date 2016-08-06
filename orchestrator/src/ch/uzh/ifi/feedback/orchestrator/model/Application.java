package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Application {
	
	private String name;
	private Timestamp createdAt;
	private Integer state;
	private Integer id;
	private List<GeneralConfiguration> general_configurations;
	private List<Configuration> configurations;
	
	public Application()
	{
		general_configurations = new ArrayList<>();
		configurations = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public List<GeneralConfiguration> getGeneralConfigurations() {
		return general_configurations;
	}

	public List<Configuration> getConfigurations() {
		return configurations;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
