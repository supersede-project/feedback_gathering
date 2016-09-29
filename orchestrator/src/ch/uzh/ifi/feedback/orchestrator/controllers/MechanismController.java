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

@Controller(FeedbackMechanism.class)
public class MechanismController extends RestController<FeedbackMechanism> {

	@Inject
	public MechanismController(MechanismSerializationService serializationService,
			MechanismService dbService) {
		super(serializationService, dbService);
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
	@Path("Configurations/{config_id}/mechanisms")
	public List<FeedbackMechanism> GetAllByConfiguration( @PathParam("config_id")Integer configId) throws Exception 
	{
		return super.GetAllFor("configuration_id", configId);
	}
	
	@PUT
	@Path("Configurations/{config_id}/mechanisms")
	public FeedbackMechanism UpdateMechanismForConfiguration(@PathParam("config_id")Integer configId, FeedbackMechanism mechanism) throws Exception 
	{
		super.UpdateFor(mechanism, "configuration_id", configId);
		return mechanism;
	}
	
	@POST
	@Path("Configurations/{config_id}/mechanisms")
	public FeedbackMechanism InsertMechanismForConfiguration(@PathParam("config_id")Integer configId, FeedbackMechanism mechanism) throws Exception 
	{
		super.InsertFor(mechanism, "configuration_id", configId);
		return mechanism;
	}
}
