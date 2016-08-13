package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.library.rest.validation.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ConfigurationValidator;

@Validate(ConfigurationValidator.class)
@Serialize(ConfigurationSerializationService.class)
public class Configuration implements IDbItem {
	
	private String name;
	@Id
	private Integer id;
	private Timestamp createdAt;
	@NotNull
	private ConfigurationType type;
	private List<FeedbackMechanism> mechanisms;
	private GeneralConfiguration generalConfiguration;
	
	private transient Integer generalConfigurationId;
	
	public Configuration(){
		mechanisms = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String applicationName) {
		this.name = applicationName;
	}

	public List<FeedbackMechanism> getFeedbackMechanisms() {
		return mechanisms;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public ConfigurationType getType() {
		return type;
	}

	public void setType(ConfigurationType type) {
		this.type = type;
	}

	public GeneralConfiguration getGeneralConfiguration() {
		return generalConfiguration;
	}

	public void setGeneralConfiguration(GeneralConfiguration generalConfiguration) {
		this.generalConfiguration = generalConfiguration;
	}

	public Integer getGeneralConfigurationId() {
		return generalConfigurationId;
	}

	public void setGeneralConfigurationId(Integer generalConfigurationId) {
		this.generalConfigurationId = generalConfigurationId;
	}
	
}
