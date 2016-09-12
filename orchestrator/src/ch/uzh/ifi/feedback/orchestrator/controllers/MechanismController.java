package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.serialization.MechanismSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.validation.MechanismValidator;

@Controller(FeedbackMechanism.class)
public class MechanismController extends RestController<FeedbackMechanism> {

	@Inject
	public MechanismController(MechanismService dbService, MechanismValidator validator) {
		super(dbService, validator);
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
	@Path("/configurations/{config_id}/mechanisms")
	public FeedbackMechanism UpdateMechanismForConfiguration(@PathParam("config_id")Integer configId, FeedbackMechanism mechanism) throws Exception 
	{
		mechanism.setConfigurationsid(configId);
		super.Update(mechanism);
		return mechanism;
	}
	
	@POST
	@Path("/configurations/{config_id}/mechanisms")
	public FeedbackMechanism InsertMechanismForConfiguration(@PathParam("config_id")Integer configId, FeedbackMechanism mechanism) throws Exception 
	{
		mechanism.setConfigurationsid(configId);
		super.Insert(mechanism);
		return mechanism;
	}
}
