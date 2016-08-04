package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterListSerializationService;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;

@Controller
public class ParameterController {
	
	private ParameterService parameterService;
	
	public ParameterController() {
		this.parameterService = new ParameterService();
	}
	
	@GET
	@Path("/parameters/{parameter_id}")
	@Serialize(ParameterSerializationService.class)
	public FeedbackParameter GetParameterById( @PathParam("parameter_id") Integer id) throws Exception 
	{
		FeedbackParameter param = parameterService.GetParameterById(TransactionManager.createDatabaseConnection(), id);		
		return param;
	}
	
	@GET
	@Path("/parameters")
	@Serialize(ParameterListSerializationService.class)
	public List<FeedbackParameter> GetAllParameters() throws Exception 
	{
		final List<FeedbackParameter> params = parameterService.GetAllParameters(TransactionManager.createDatabaseConnection());
		return params;
	}
	
	@GET
	@Path("/mechanisms/{mechanism_id}/parameters")
	@Serialize(ParameterListSerializationService.class)
	public List<FeedbackParameter> GetParametersByMechanism(@PathParam("mechanism_id") Integer id) throws Exception 
	{
		final List<FeedbackParameter> params = parameterService.GetParametersFor(
				TransactionManager.createDatabaseConnection(), 
				"mechanisms", 
				"mechanism_id", 
				id);

		return params;
	}
	
	@GET
	@Path("/general_configurations/{config_id}/parameters")
	@Serialize(ParameterListSerializationService.class)
	public List<FeedbackParameter> GetParametersByGeneralConfigurationId(@PathParam("config_id") Integer id) throws Exception 
	{
		final List<FeedbackParameter> params = parameterService.GetParametersFor(
				TransactionManager.createDatabaseConnection(), 
				"general_configurations", 
				"general_configuration_id", 
				id);
		
		return params;
	}
	
	@PUT
	@Path("/parameters")
	@Serialize(ParameterSerializationService.class)
	public FeedbackParameter UpdateParameter(FeedbackParameter param) throws Exception 
	{
		TransactionManager.withTransaction((con) -> {
			parameterService.UpdateParameter(param, null, null, null, con);
		});
		
		return parameterService.GetParameterById(TransactionManager.createDatabaseConnection(), param.getId());
	}

	@POST
	@Path("/general_configurations/{config_id}/parameters")
	@Serialize(ParameterSerializationService.class)
	public FeedbackParameter InsertParameterForConfiguration(@PathParam("config_id")Integer config_id, final FeedbackParameter param) throws Exception 
	{
		TransactionManager.withTransaction((con) -> {
			parameterService.InsertParameter(param, null, null, null, con);
		});
		
		return param;
	}
	
	@POST
	@Path("/mechanisms/{mechanism_id}/parameters")
	@Serialize(ParameterSerializationService.class)
	public FeedbackParameter InsertParameterForMechanism(@PathParam("mechanism_id") Integer mechanism_id, final FeedbackParameter param) throws Exception 
	{
		TransactionManager.withTransaction((con) -> {
			parameterService.InsertParameter(param, null, null, null, con);
		});
		
		return param;
	}
}
