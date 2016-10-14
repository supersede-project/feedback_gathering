package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.User;
import ch.uzh.ifi.feedback.orchestrator.services.UserService;
import ch.uzh.ifi.feedback.orchestrator.validation.UserValidator;

@RequestScoped
@Controller(User.class)
public class UserController extends RestController<User> {

	@Inject
	public UserController(UserService dbService, UserValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@GET
	@Path("/users")
	public List<User> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	@Path("/user_groups/{group_id}/users")
	public User InsertForGroup(@PathParam("group_id") Integer groupId, User user) throws Exception 
	{
		user.setGroupId(groupId);
		return super.Insert(user);
	}
	
	@PUT
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	@Path("/users")
	public User UpdateForGroup(User user) throws Exception 
	{
		return super.Update(user);
	}
}
