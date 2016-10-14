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
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.serialization.GeneralConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.validation.GeneralConfigurationValidator;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

@RequestScoped
@Controller(GeneralConfiguration.class)
public class GeneralConfigurationController extends RestController<GeneralConfiguration> 
{
	private ApplicationService applicationService;
	private ConfigurationService configurationService;
	
	@Inject
	public GeneralConfigurationController(
			GeneralConfigurationService dbService, 
			ApplicationService applicationService,
			ConfigurationService configurationService,
			HttpServletRequest request, 
			HttpServletResponse response,
			GeneralConfigurationValidator validator) 
	{
		super(dbService, validator, request, response);
		
		this.applicationService = applicationService;
		this.configurationService = configurationService;
	}
	
	@GET
	@Path("/general_configurations/{config_id}")
	public GeneralConfiguration GetById( @PathParam("config_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/applications/{app_id}/general_configuration")
	public GeneralConfiguration GetByApplication( @PathParam("app_id")Integer appId) throws Exception 
	{
		Application app = applicationService.GetById(appId);
		return app.getGeneralConfiguration();
	}
	
	@GET
	@Path("/configurations/{config_id}/general_configuration")
	public GeneralConfiguration GetByConfiguration( @PathParam("config_id")Integer configId) throws Exception 
	{
		Configuration config = configurationService.GetById(configId);
		return config.getGeneralConfiguration();
	}
	
	@PUT
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/general_configurations")
	public GeneralConfiguration UpdateGeneralConfigurationForApplication(
			@PathParam("application_id") Integer applicationId,
			GeneralConfiguration config) throws Exception 
	{
		Application app = applicationService.GetById(applicationId);
		if(!app.getGeneralConfiguration().getId().equals(config.getId()))
			throw new NotFoundException("the configuration does not belong to the specified application");
		
		return super.Update(config);
	}
	

	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/general_configuration")
	public GeneralConfiguration InsertGeneralConfigurationForApplication(
			@PathParam("app_id")Integer appId, 
			GeneralConfiguration config) throws Exception 
	{
		Application app = applicationService.GetById(appId);
		if(app.getGeneralConfiguration() != null)
			throw new ValidationException("general configuration for application already set. Please perform an update!");
		
		super.Validate(config, false);
		app.setGeneralConfiguration(config);
		
		TransactionManager.withTransaction((con) -> 
			applicationService.Update(con, app)
		);
		
		return applicationService.GetById(appId).getGeneralConfiguration();
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/configurations/{config_id}/general_configuration")
	public GeneralConfiguration InsertGeneralConfigurationForConfiguration(
			@PathParam("config_id")Integer configId, 
			@PathParam("application_id") Integer applicationId,
			GeneralConfiguration config) throws Exception 
	{
		Configuration configuration = configurationService.GetById(configId);
		
		if(configuration.getGeneralConfiguration() != null)
			throw new ValidationException("general configuration for configuration already set. Please perform update!");
		
		if(!configuration.getApplicationId().equals(applicationId))
			throw new NotFoundException("the configuration does not belong to the specified application");
		
		super.Validate(config, false);
		configuration.setGeneralConfiguration(config);
		
		TransactionManager.withTransaction((con) -> 
			configurationService.Update(con, configuration)
	    );
		
		return configurationService.GetById(configId).getGeneralConfiguration();
	}
}
