package ch.uzh.ifi.feedback.orchestrator.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;
import ch.uzh.ifi.feedback.library.rest.validation.Unique;
import ch.uzh.ifi.feedback.library.rest.validation.Validate;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.GeneralConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.validation.GeneralConfigurationValidator;

@Validate(GeneralConfigurationValidator.class)
@Serialize(GeneralConfigurationSerializationService.class)
public class GeneralConfiguration implements IDbItem {
	
	@DbAttribute("created_at")
	private Timestamp createdAt;
	@DbAttribute("updated_at")
	private Timestamp updatedAt;
	private List<FeedbackParameter> parameters;
	@Unique
	private String name;
	@Id
	private Integer id;
	
	public GeneralConfiguration()
	{
		parameters = new ArrayList<FeedbackParameter>();
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp created_at) {
		this.createdAt = created_at;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updated_at) {
		this.updatedAt = updated_at;
	}
	public List<FeedbackParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<FeedbackParameter> parameters) {
		this.parameters = parameters;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
