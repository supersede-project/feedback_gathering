package ch.uzh.ifi.feedback.repository.model;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;
import ch.uzh.ifi.feedback.repository.serialization.StatusOptionSerializationService;

@Serialize(StatusOptionSerializationService.class)
public class StatusOption extends ItemBase<StatusOption>{

	@Id
	private Integer id;
	
	@NotNull
	private String name;

	@NotNull
	private int order;
	
	@DbAttribute("user_specific")
	@NotNull
	private boolean userSpecific;
	
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isUserSpecific() {
		return userSpecific;
	}

	public void setUserSpecific(boolean userSpecific) {
		this.userSpecific = userSpecific;
	}

}
