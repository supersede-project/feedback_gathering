package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorType;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorTypeService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorTypeValidator;

@RequestScoped
@Controller(MonitorTypeController.class)
public class MonitorTypeController extends RestController<MonitorType>{

	@Inject
	public MonitorTypeController(MonitorTypeService dbService, MonitorTypeValidator validator, HttpServletRequest request,
			HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@GET
	@Path("/monitors")
	public List<MonitorType> GetAll() throws Exception {
		return super.GetAll();
	}
	
	@POST
	@Path("/monitors")
	public MonitorType InsertMonitorType(MonitorType monitorType) throws Exception {
		return super.Insert(monitorType);
	}
	
	@GET
	@Path("/monitors/{id-type-of-monitor}")
	public MonitorType GetById(@PathParam("id-type-of-monitor") Integer id) throws Exception {
		return super.GetById(id);
	}
	
	@DELETE
	@Path("/monitors/{id-type-of-monitor}")
	public void DeleteMonitorTool(@PathParam("id-type-of-monitor") Integer id) throws Exception {
		super.Delete(id);
	}
	
	
}
