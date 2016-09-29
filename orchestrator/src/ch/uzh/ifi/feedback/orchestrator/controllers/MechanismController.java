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
import ch.uzh.ifi.feedback.orchestrator.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.serialization.MechanismSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.validation.MechanismValidator;
import static java.util.Arrays.asList;

@Controller(FeedbackMechanism.class)
public class MechanismController extends RestController<FeedbackMechanism> {

	@Inject
	public MechanismController(MechanismService dbService, MechanismValidator validator, IRequestContext requestContext) {
		super(dbService, validator, requestContext);
	}

	@GET
	@Path("/mechanisms/{mechanism_id}")
	public FeedbackMechanism GetById( @PathParam("mechanism_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/mechanisms")
	public List<FeedbackMechanism> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@GET
	@Path("/configurations/{config_id}/mechanisms")
	public List<FeedbackMechanism> GetAllByConfiguration( @PathParam("config_id")Integer configId) throws Exception 
	{
		return super.GetAllFor("configurations_id", configId);
	}
	
	@PUT
	@Authenticate(UserAuthenticationService.class)
	@Path("/configurations/{config_id}/mechanisms")
	public FeedbackMechanism UpdateMechanismForConfiguration(@PathParam("config_id")Integer configId, FeedbackMechanism mechanism) throws Exception 
	{
		mechanism.setConfigurationsid(configId);
		super.Update(mechanism);
		return dbService.GetWhere(asList(configId, mechanism.getId()), "cm.configurations_id = ?", "t.mechanisms_id = ?").get(0);
	}
	
	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/configurations/{config_id}/mechanisms")
	public FeedbackMechanism InsertMechanismForConfiguration(@PathParam("config_id")Integer configId, FeedbackMechanism mechanism) throws Exception 
	{
		mechanism.setConfigurationsid(configId);
		FeedbackMechanism created = super.Insert(mechanism);
		return dbService.GetWhere(asList(configId, created.getId()), "cm.configurations_id = ?", "t.mechanisms_id = ?").get(0);
	}
}
