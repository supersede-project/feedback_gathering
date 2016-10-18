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
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.GeneralConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;
import ch.uzh.ifi.feedback.orchestrator.validation.ParameterValidator;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

@RequestScoped
@Controller(FeedbackParameter.class)
public class ParameterController extends RestController<FeedbackParameter> {
	
	private MechanismService mechanismService;
	private ConfigurationService configurationService;
	private GeneralConfigurationService generalConfigurationService;
	private ApplicationService applciationService;

	@Inject
	public ParameterController(
			ParameterService dbService, 
			MechanismService mechanismService,
			ConfigurationService configurationService,
			GeneralConfigurationService generalConfigurationService,
			ApplicationService applciationService,
			ParameterValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
		this.mechanismService = mechanismService;
		this.configurationService = configurationService;
		this.generalConfigurationService = generalConfigurationService;
		this.applciationService = applciationService;
	}

	@GET
	@Path("/parameters/{parameter_id}")
	public FeedbackParameter GetParameterById( @PathParam("parameter_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/parameters")
	public List<FeedbackParameter> GetAllParameters() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/mechanisms/{mechanism_id}/parameters")
	public List<FeedbackParameter> GetParametersByMechanism(@PathParam("mechanism_id") Integer id) throws Exception 
	{
		return super.GetAllFor("mechanisms_id", id);
	}
	
	@GET
	@Path("/general_configurations/{config_id}/parameters")
	public List<FeedbackParameter> GetParametersByGeneralConfigurationId(@PathParam("config_id") Integer id) throws Exception 
	{
		return super.GetAllFor("general_configurations_id", id);
	}
	
	@PUT
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/parameters")
	public FeedbackParameter UpdateParameter(
			@PathParam("application_id")Integer applicationId, 		
			FeedbackParameter param) throws Exception 
	{
		FeedbackParameter parameter = super.GetById(param.getId());
		ValidateApplication(parameter, applicationId);
		return super.Update(param);
	}

	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/general_configurations/{config_id}/parameters")
	public FeedbackParameter InsertParameterForGeneralConfiguration(
			@PathParam("config_id")Integer config_id, 
			@PathParam("application_id")Integer applicationId, 
			final FeedbackParameter param) throws Exception 
	{
		param.setGenaralConfigurationId(config_id);
		ValidateApplication(param, applicationId);
		return super.Insert(param);
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/applications/{application_id}/mechanisms/{mechanism_id}/parameters")
	public FeedbackParameter InsertParameterForMechanism(
			@PathParam("mechanism_id") Integer mechanism_id, 
			@PathParam("application_id")Integer applicationId, 
			final FeedbackParameter param) throws Exception 
	{
		param.setMechanismId(mechanism_id);
		ValidateApplication(param, applicationId);
		return super.Insert(param);
	}
	
	private void ValidateApplication(FeedbackParameter param, Integer applicationId) throws Exception
	{
		if(param.getMechanismId() != null)
		{
			List<FeedbackMechanism> mechanisms = mechanismService.GetWhere(
					asList(param.getMechanismId()), "mechanisms_id = ?");
			
			boolean valid = false;
			for(FeedbackMechanism mechanism : mechanisms)
			{
				Configuration config = configurationService.GetById(mechanism.getConfigurationsid());
				if(config.getApplicationId().equals(applicationId))
					valid = true;
			}
			if(!valid)
				throw new NotFoundException("the parameter does not belong to the specified application");
			
		}else if(param.getGenaralConfigurationId() != null)
		{
			GeneralConfiguration config = generalConfigurationService.GetById(param.getGenaralConfigurationId());
			List<Configuration> configs = configurationService.GetWhere(asList(config.getId()), "general_configurations_id = ?");
			if(!configs.isEmpty())
			{
				if(!configs.get(0).getApplicationId().equals(applicationId))
					throw new NotFoundException("the parameter does not belong to the specified application");
			}else{
				List<Application> apps = applciationService.GetWhere(asList(config.getId()), "general_configurations_id = ?");
				if(!apps.isEmpty())
				{
					if(!apps.get(0).getId().equals(applicationId))
						throw new NotFoundException("the parameter does not belong to the specified application");
				}
			}
		}
	}
}
