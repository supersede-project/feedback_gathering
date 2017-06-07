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
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.serialization.MechanismSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.validation.MechanismValidator;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

@RequestScoped
@Controller(FeedbackMechanism.class)
public class MechanismController extends RestController<FeedbackMechanism> {

	private ConfigurationService configurationService;

	@Inject
	public MechanismController(
			MechanismService dbService, 
			ConfigurationService configurationService,
			MechanismValidator validator,
			HttpServletRequest request, 
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
		this.configurationService = configurationService;
	}

	@GET
	@Path("/{lang}/mechanisms/{mechanism_id}")
	public FeedbackMechanism GetById( @PathParam("mechanism_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/{lang}/mechanisms")
	public List<FeedbackMechanism> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/{lang}/configurations/{config_id}/mechanisms")
	public List<FeedbackMechanism> GetAllByConfiguration( @PathParam("config_id")Integer configId) throws Exception 
	{
		return super.GetAllFor("configurations_id", configId);
	}
	
	@PUT
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/{lang}/applications/{application_id}/configurations/{config_id}/mechanisms")
	public FeedbackMechanism UpdateMechanismForConfiguration(
			@PathParam("config_id")Integer configId, 
			@PathParam("application_id") Integer applicationId,
			FeedbackMechanism mechanism) throws Exception 
	{
		Configuration config = configurationService.GetById(configId);
		if(!config.getApplicationId().equals(applicationId))
			throw new NotFoundException("the configuration cannot be found in the specified application");
			
		mechanism.setConfigurationsid(configId);
		super.Update(mechanism);
		return dbService.GetWhere(asList(configId, mechanism.getId()), "cm.configurations_id = ?", "t.mechanisms_id = ?").get(0);
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	@Path("/{lang}/applications/{application_id}/configurations/{config_id}/mechanisms")
	public FeedbackMechanism InsertMechanismForConfiguration(
			@PathParam("config_id")Integer configId,
			@PathParam("application_id") Integer applicationId,
			FeedbackMechanism mechanism) throws Exception 
	{
		Configuration config = configurationService.GetById(configId);
		if(!config.getApplicationId().equals(applicationId))
			throw new NotFoundException("the configuration cannot be found in the specified application");
		
		mechanism.setConfigurationsid(configId);
		FeedbackMechanism created = super.Insert(mechanism);
		return dbService.GetWhere(asList(configId, created.getId()), "cm.configurations_id = ?", "t.mechanisms_id = ?").get(0);
	}
}
