package ch.uzh.ifi.feedback.orchestrator.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorToolService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorToolValidator;

@RequestScoped
@Controller(MonitorToolController.class)
public class MonitorToolController extends RestController<MonitorTool> {

	public MonitorToolController(MonitorToolService dbService, 
			MonitorToolValidator validator,
			HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@POST
	@Path("/monitors/{id-type-of-monitor}")
	public MonitorTool InsertMonitorTool(@PathParam("id-type-of-monitor") Integer id, 
			MonitorTool tool) throws Exception {
		tool.setMonitorTypeId(id);
		return super.Insert(tool);
	}
	
	@GET
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}")
	public MonitorTool GetMonitorTool(@PathParam("id-type-of-monitor") Integer type,
			@PathParam("id-monitoring-tool") Integer tool) throws Exception {
		return super.GetById(tool);
	}
	
	@DELETE
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}")
	public void DeleteTool(@PathParam("id-type-of-monitor") Integer type,
			@PathParam("id-monitoring-tool") Integer tool) throws Exception {
		super.Delete(tool);
	}
	

}
