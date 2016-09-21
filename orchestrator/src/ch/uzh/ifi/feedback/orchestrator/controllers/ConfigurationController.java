package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.orchestrator.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.User;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.UserGroupService;
import ch.uzh.ifi.feedback.orchestrator.services.UserService;
import ch.uzh.ifi.feedback.orchestrator.validation.ConfigurationValidator;
import static java.util.Arrays.asList;

@Controller(Configuration.class)
public class ConfigurationController extends RestController<Configuration>
{
	private UserService userService;
	private UserGroupService userGroupService;
	
	@Inject
	public ConfigurationController(
			ConfigurationService dbService, 
			ConfigurationValidator validator, 
			UserService userService,
			UserGroupService userGroupService) {
		
		super(dbService, validator);
		this.userService = userService; 
		this.userGroupService = userGroupService;
	}
	
	@GET
	@Path("/configurations/{config_id}")
	public Configuration GetById( @PathParam("config_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/configurations")
	public List<Configuration> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/applications/{app_id}/configurations")
	public List<Configuration> GetAllByApplication( @PathParam("app_id")Integer appId) throws Exception 
	{
		return super.GetAllFor("applications_id", appId);
	}
	
	@GET
	@Path("/applications/{app_id}/users/{user_id}/configurations")
	public List<Configuration> GetAllByUserNameAndApplication( 
			@PathParam("user_id")Integer userId, 
			@PathParam("app_id")Integer appId) throws Exception 
	{
		List<User> users = userService.GetWhere(asList(userId), "users_id = ?");
		if(users.size() != 1)
			throw new ValidationException("The user you provided does not exist!");
		
		int groupId = users.get(0).getGroupId();
		return dbService.GetWhere(asList(groupId, appId), "user_groups_id = ?", "applications_id = ?");
	}
	
	@GET
	@Path("/applications/{app_id}/user_groups/{group_id}/configurations")
	public List<Configuration> GetAllByUserGroupAndApplication( 
			@PathParam("group_id")Integer groupId,
			@PathParam("app_id") Integer appId) throws Exception 
	{
		return dbService.GetWhere(asList(groupId, appId), "user_groups_id = ?", "applications_id = ?");
	}
	
	@PUT
	@Authenticate(UserAuthenticationService.class)
	@Path("/configurations")
	public Configuration UpdateConfiguration(Configuration config) throws Exception 
	{
		return super.Update(config);
	}
	
	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/applications/{app_id}/configurations")
	public Configuration InsertConfigurationForApplication(@PathParam("app_id")Integer appId, Configuration config) throws Exception 
	{
		//Set default user group id when no group specified
		int groupId = userGroupService.GetWhere(asList("default"), "name = ?").get(0).getId();
		config.setUserGroupsId(groupId);
		
		config.setApplicationId(appId);
		return super.Insert(config);
	}
	
	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/applications/{app_id}/user_groups/{group_id}/configurations")
	public Configuration InsertConfigurationForApplicationAndUserGroup(
			@PathParam("app_id")Integer appId, 
			@PathParam("group_id")Integer groupId,
			Configuration config) throws Exception 
	{
		config.setUserGroupsId(groupId);
		config.setApplicationId(appId);
		
		return super.Insert(config);
	}
}
