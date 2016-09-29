package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.IRequestContext;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.serialization.GeneralConfigurationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.validation.GeneralConfigurationValidator;

import static java.util.Arrays.asList;

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
			IRequestContext requestContext,
			GeneralConfigurationValidator validator) 
	{
		super(dbService, validator, requestContext);
		
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
	@Path("/general_configurations")
	public List<GeneralConfiguration> GetAll() throws Exception 
	{
		return super.GetAll();
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
	@Authenticate(UserAuthenticationService.class)
	@Path("/general_configurations")
	public GeneralConfiguration UpdateGeneralConfiguration(GeneralConfiguration config) throws Exception 
	{
		return super.Update(config);
	}
	

	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/applications/{app_id}/general_configuration")
	public GeneralConfiguration InsertGeneralConfigurationForApplication(@PathParam("app_id")Integer appId, GeneralConfiguration config) throws Exception 
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
	@Authenticate(UserAuthenticationService.class)
	@Path("/configurations/{config_id}/general_configuration")
	public GeneralConfiguration InsertGeneralConfigurationForConfiguration(@PathParam("config_id")Integer configId, GeneralConfiguration config) throws Exception 
	{
		Configuration configuration = configurationService.GetById(configId);
		if(configuration.getGeneralConfiguration() != null)
			throw new ValidationException("general configuration for configuration already set. Please perform update!");
		
		super.Validate(config, false);
		configuration.setGeneralConfiguration(config);
		
		TransactionManager.withTransaction((con) -> 
			configurationService.Update(con, configuration)
	    );
		
		return configurationService.GetById(configId).getGeneralConfiguration();
	}
}
