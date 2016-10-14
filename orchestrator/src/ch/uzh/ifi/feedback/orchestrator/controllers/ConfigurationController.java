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
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.User;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.UserGroupService;
import ch.uzh.ifi.feedback.orchestrator.services.UserService;
import ch.uzh.ifi.feedback.orchestrator.validation.ConfigurationValidator;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

@RequestScoped
@Controller(Configuration.class)
public class ConfigurationController extends RestController<Configuration>
{
	private UserService userService;
	private UserGroupService userGroupService;
	
	@Inject
	public ConfigurationController(
			ConfigurationService dbService, 
			ConfigurationValidator validator,
			HttpServletRequest request, 
			HttpServletResponse response,
			UserService userService,
			UserGroupService userGroupService) {
		
		super(dbService, validator, request, response);
		
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
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/configurations")
	public Configuration InsertConfigurationForApplication(@PathParam("application_id")Integer appId, Configuration config) throws Exception 
	{
		//Set default user group id when no group specified
		int groupId = userGroupService.GetWhere(asList("default"), "name = ?").get(0).getId();
		config.setUserGroupsId(groupId);
		
		config.setApplicationId(appId);
		return super.Insert(config);
	}
	
	@PUT
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/configurations")
	public Configuration UpdateConfigurationForApplication(
			@PathParam("application_id") Integer applicationId,
			Configuration config) throws Exception
	{
		Configuration oldConfig = dbService.GetById(config.getId());
		if(!oldConfig.getApplicationId().equals(applicationId))
			throw new NotFoundException("the configuration does not exist in the provided application");
			
		return super.Update(config);
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/user_groups/{group_id}/configurations")
	public Configuration InsertConfigurationForApplicationAndUserGroup(
			@PathParam("application_id")Integer appId, 
			@PathParam("group_id")Integer groupId,
			Configuration config) throws Exception 
	{
		config.setUserGroupsId(groupId);
		config.setApplicationId(appId);
		
		return super.Insert(config);
	}
}
