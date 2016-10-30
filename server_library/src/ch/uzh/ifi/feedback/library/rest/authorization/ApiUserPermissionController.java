package ch.uzh.ifi.feedback.library.rest.authorization;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;

@RequestScoped
@Controller(ApiUserPermission.class)
public class ApiUserPermissionController extends RestController<ApiUserPermission> {

	@Inject
	public ApiUserPermissionController(
			ApiUserPermissionService dbService, 
			ApiUserPermissionValidator validator,
			HttpServletRequest request, 
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
	}

	@GET
	@Path("/{lang}/api_users/{user_id}/permissions")
	public List<ApiUserPermission> GetAllForUser(@PathParam("user_id") Integer userId) throws Exception
	{
		return super.GetAllFor("user_id", userId);
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	@Path("/{lang}/api_users/{user_id}/permissions")
	public ApiUserPermission InsertForUser(@PathParam("user_id") Integer userId, ApiUserPermission permission) throws Exception
	{
		permission.setUserId(userId);
		return super.Insert(permission);
	}
	
	@DELETE
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	@Path("/{lang}/api_users/permissions/{permission_id}")
	public void DeleteById(@PathParam("permission_id") Integer permissionId) throws Exception
	{
		super.Delete(permissionId);
	}
}
