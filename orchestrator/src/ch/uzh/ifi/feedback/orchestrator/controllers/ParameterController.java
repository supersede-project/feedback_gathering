package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;

@Controller(FeedbackParameter.class)
public class ParameterController extends RestController<FeedbackParameter> {
	
	@Inject
	public ParameterController(ParameterSerializationService serializationService,
			ParameterService dbService) {
		super(serializationService, dbService);
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
		return super.GetAllFor("configurations_id", id);
	}
	
	@PUT
	@Path("/parameters")
	public FeedbackParameter UpdateParameter(FeedbackParameter param) throws Exception 
	{
		super.Update(param);
		return param;
	}

	@POST
	@Path("/general_configurations/{config_id}/parameters")
	public FeedbackParameter InsertParameterForConfiguration(@PathParam("config_id")Integer config_id, final FeedbackParameter param) throws Exception 
	{
		param.setGenaralConfigurationId(config_id);
		super.Insert(param);
		return param;
	}
	
	@POST
	@Path("/mechanisms/{mechanism_id}/parameters")
	public FeedbackParameter InsertParameterForMechanism(@PathParam("mechanism_id") Integer mechanism_id, final FeedbackParameter param) throws Exception 
	{
		param.setMechanismId(mechanism_id);
		super.Insert(param);
		return param;
	}
}
