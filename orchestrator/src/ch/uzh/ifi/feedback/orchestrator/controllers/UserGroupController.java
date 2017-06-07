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
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;
import ch.uzh.ifi.feedback.orchestrator.services.UserGroupService;
import ch.uzh.ifi.feedback.orchestrator.validation.UserGroupValidator;

@RequestScoped
@Controller(UserGroup.class)
public class UserGroupController extends RestController<UserGroup> {
	
	@Inject
	public UserGroupController(UserGroupService dbService, UserGroupValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@GET
	@Path("/{lang}/user_groups")
	public List<UserGroup> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/{lang}/user_groups/{group_id}")
	public UserGroup GetById(@PathParam("group_id") Integer groupId) throws Exception 
	{
		return super.GetById(groupId);
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, role = "ADMIN")
	@Path("/{lang}/user_groups")
	public UserGroup Insert(UserGroup group) throws Exception
	{
		return super.Insert(group);
	}
}
