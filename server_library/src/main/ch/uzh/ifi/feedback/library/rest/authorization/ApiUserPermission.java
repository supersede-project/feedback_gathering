package ch.uzh.ifi.feedback.library.rest.authorization;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.annotations.NotNull;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;

@Serialize(ApiUserPermissionSerializationService.class)
public class ApiUserPermission extends ItemBase<ApiUserPermission> {

	@Id
	private Integer id;
	
	@DbAttribute("application_id")
	@NotNull
	private Integer applicationId;
	
	@DbAttribute("user_id")
	@NotNull
	private transient Integer userId;
	
	@DbAttribute("has_permission")
	@NotNull
	private boolean hasPermission;
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public boolean isHasPermission() {
		return hasPermission;
	}

	public void setHasPermission(boolean hasPermission) {
		this.hasPermission = hasPermission;
	}

}
