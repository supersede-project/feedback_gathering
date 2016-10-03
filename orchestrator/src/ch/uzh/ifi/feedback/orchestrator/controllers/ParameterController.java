package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.IRequestContext;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.serialization.ParameterSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ParameterService;
import ch.uzh.ifi.feedback.orchestrator.validation.ParameterValidator;

@RequestScoped
@Controller(FeedbackParameter.class)
public class ParameterController extends RestController<FeedbackParameter> {
	
	@Inject
	public ParameterController(ParameterService dbService, ParameterValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
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
	@Authenticate(UserAuthenticationService.class)
	@Path("/parameters")
	public FeedbackParameter UpdateParameter(FeedbackParameter param) throws Exception 
	{
		return super.Update(param);
	}

	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/general_configurations/{config_id}/parameters")
	public FeedbackParameter InsertParameterForConfiguration(@PathParam("config_id")Integer config_id, final FeedbackParameter param) throws Exception 
	{
		param.setGenaralConfigurationId(config_id);
		return super.Insert(param);
	}
	
	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/mechanisms/{mechanism_id}/parameters")
	public FeedbackParameter InsertParameterForMechanism(@PathParam("mechanism_id") Integer mechanism_id, final FeedbackParameter param) throws Exception 
	{
		param.setMechanismId(mechanism_id);
		return super.Insert(param);
	}
}
