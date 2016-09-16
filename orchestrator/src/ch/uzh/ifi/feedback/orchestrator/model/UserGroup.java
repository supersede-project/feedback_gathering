package ch.uzh.ifi.feedback.orchestrator.model;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.validation.Id;
import ch.uzh.ifi.feedback.library.rest.validation.NotNull;

public class UserGroup extends OrchestratorItem<UserGroup>{

	@Id
	private Integer id;
	
	@NotNull
	private String name;
	
	@DbIgnore
	private List<User> users;
	
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

	public List<User> getUsers() {
		if(users == null)
			users = new ArrayList<>();
		
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
