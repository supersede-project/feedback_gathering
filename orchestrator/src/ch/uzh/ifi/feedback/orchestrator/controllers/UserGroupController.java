package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;
import ch.uzh.ifi.feedback.orchestrator.services.UserGroupService;
import ch.uzh.ifi.feedback.orchestrator.validation.UserGroupValidator;

@Controller(UserGroup.class)
public class UserGroupController extends RestController<UserGroup> {
	
	@Inject
	public UserGroupController(UserGroupService dbService, UserGroupValidator validator) {
		super(dbService, validator);
	}
	
	@GET
	@Path("/user_groups")
	public List<UserGroup> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@POST
	@Path("/user_groups")
	public UserGroup Insert(UserGroup group) throws Exception 
	{
		return super.Insert(group);
	}
	
	@PUT
	@Path("/user_groups")
	public UserGroup UpdateGroup(UserGroup group) throws Exception 
	{
		return super.Update(group);
	}
}
