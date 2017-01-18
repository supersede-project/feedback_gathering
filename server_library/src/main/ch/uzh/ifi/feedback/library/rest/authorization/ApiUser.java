package ch.uzh.ifi.feedback.library.rest.authorization;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Unique;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;

@Serialize(ApiUserSerializationService.class)
public class ApiUser extends ItemBase<ApiUser> {

	@Id
	private Integer id;
	
	@Unique
	@NotNull
	private String name;
	
	@NotNull
	private String password;
	
	private UserRole role;
	
	@DbIgnore
	private List<ApiUserPermission> permissions;
	 
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

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public List<ApiUserPermission> getPermissions() {
		if(permissions == null)
			permissions = new ArrayList<>();
		
		return permissions;
	}

	public void setPermissions(List<ApiUserPermission> permissions) {
		this.permissions = permissions;
	}
}
