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
import ch.uzh.ifi.feedback.orchestrator.model.Application;
import ch.uzh.ifi.feedback.orchestrator.serialization.ApplicationSerializationService;
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;

@Controller(Application.class)
public class ApplicationController extends RestController<Application> {

	@Inject
	public ApplicationController(ApplicationSerializationService serializationService,
			ApplicationService dbService) {
		super(serializationService, dbService);
	}
	
	@GET
	@Path("/applications/{app_id}")
	public Application GetById( @PathParam("app_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}
	
	@GET
	@Path("/applications")
	public List<Application> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@PUT
	@Path("/applications")
	public Application UpdateApplication(Application app) throws Exception 
	{
		super.Update(app);
		return app;
	}
	
	@POST
	@Path("/applications")
	public Application InsertApplication(@PathParam("app_id")Integer appId, Application app) throws Exception 
	{
		super.Insert(app);
		return app;
	}
}
