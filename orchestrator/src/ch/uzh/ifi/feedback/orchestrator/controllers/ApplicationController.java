package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import ch.uzh.ifi.feedback.orchestrator.services.ApplicationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ApplicationValidator;
import integration.DataProviderIntegratorOrchestrator;

@RequestScoped
@Controller(Application.class)
public class ApplicationController extends RestController<Application> {

	private DataProviderIntegratorOrchestrator dataProviderIntegratorOrchestrator;
	private Gson gson;
	
	@Inject
	public ApplicationController(ApplicationService dbService, ApplicationValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
		this.dataProviderIntegratorOrchestrator = new DataProviderIntegratorOrchestrator();
		this.gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
	}
	
	@GET
	@Path("/{lang}/applications/{app_id}")
	public Application GetById(@PathParam("app_id") Integer id) throws Exception 
	{
		return super.GetById(id);
	}

	@GET
	@Path("/{lang}/applications")
	public List<Application> GetAll() throws Exception 
	{
		return super.GetAll();
	}
	
	@PUT
	@Authenticate(service = UserAuthenticationService.class)
	@Path("/{lang}/applications")
	public Application UpdateApplication(Application app) throws Exception 
	{
		Application updatedApplication = super.Update(app);
		updateDataProvider(updatedApplication);
		return updatedApplication;
	}
	
	@POST
	@Authenticate(service = UserAuthenticationService.class)
	@Path("/{lang}/applications")
	public Application InsertApplication(@PathParam("app_id")Integer appId, Application app) throws Exception 
	{
		Application createdApplication = super.Insert(app);
		updateDataProvider(createdApplication);
		return createdApplication;
	}
	
	private void updateDataProvider(Application application) {
		// WP2 communication
		String topicIdOrchestratorApplication = "b61ebbf1-aa9b-4e7d-8132-fec6f1e4ced9"; // v1
		String json = gson.toJson(application);		
		JSONObject jsonData = new JSONObject(json);
		try {
			dataProviderIntegratorOrchestrator.ingestJsonData(jsonData, topicIdOrchestratorApplication);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
