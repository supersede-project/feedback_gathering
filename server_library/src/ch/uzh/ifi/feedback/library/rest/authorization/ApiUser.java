package ch.uzh.ifi.feedback.library.rest.authorization;

import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;

@Serialize(ApiUserSerializationService.class)
public class ApiUser extends ItemBase<ApiUser> {

	@Id
	private Integer id;
	
	@Unique
	private String name;
	
	private String password;
	
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
