package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import ch.uzh.ifi.feedback.library.rest.IRequestContext;
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
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ApplicationValidator;

@RequestScoped
@Controller(Application.class)
public class ApplicationController extends OrchestratorController<Application> {

	@Inject
	public ApplicationController(ApplicationService dbService, ApplicationValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@GET
	@Path("/applications/{app_id}")
	public Application GetById(@PathParam("app_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/applications/{app_id}/timestamp/{time}")
	public Application GetByIdAndTime(@PathParam("app_id") Integer id, @PathParam("time") Timestamp time) throws Exception 
	{
		return super.GetByIdAndTime(id, time);
	}
	
	@GET
	@Path("/applications")
	public List<Application> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@PUT
	@Authenticate(UserAuthenticationService.class)
	@Path("/applications")
	public Application UpdateApplication(Application app) throws Exception 
	{
		return super.Update(app);
	}
	
	@POST
	@Authenticate(UserAuthenticationService.class)
	@Path("/applications")
	public Application InsertApplication(@PathParam("app_id")Integer appId, Application app) throws Exception 
	{
		return super.Insert(app);
	}
}
