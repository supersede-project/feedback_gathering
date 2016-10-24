package ch.uzh.ifi.feedback.orchestrator.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorConfigurationValidator;

@RequestScoped
@Controller(MonitorConfigurationController.class)
public class MonitorConfigurationController extends RestController<MonitorConfiguration> {

	public MonitorConfigurationController(MonitorConfigurationService dbService,
			MonitorConfigurationValidator validator, 
			HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@POST
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}")
	public MonitorConfiguration InsertMonitorConfiguration(@PathParam("id-type-of-monitor") Integer type, 
			@PathParam("id-monitoring-tool") Integer tool,
			MonitorConfiguration configuration) throws Exception {
		configuration.setMonitorToolId(tool);
		return super.Insert(configuration);
	}
	
	@GET
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}/{id-tool-configuration}")
	public MonitorConfiguration GetMonitorConfiguration(@PathParam("id-type-of-monitor") Integer type, 
			@PathParam("id-monitoring-tool") Integer tool,
			@PathParam("id-tool-configuration") Integer configuration) throws Exception {
		return super.GetById(configuration);
	}
	
	@PUT
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}/{id-tool-configuration}")
	public MonitorConfiguration UpdateMonitorConfiguration(@PathParam("id-type-of-monitor") Integer type, 
			@PathParam("id-monitoring-tool") Integer tool,
			@PathParam("id-tool-configuration") Integer configuration,
			MonitorConfiguration monitorConfiguration) throws Exception {
		return super.Insert(monitorConfiguration);
	}
	
	@DELETE
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}/{id-tool-configuration}")
	public void DeleteMonitorConfiguration(@PathParam("id-type-of-monitor") Integer type, 
			@PathParam("id-monitoring-tool") Integer tool,
			@PathParam("id-tool-configuration") Integer configuration) throws Exception {
		super.Delete(configuration);
	}

}
