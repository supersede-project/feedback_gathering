package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.repository.model.Status;
import ch.uzh.ifi.feedback.repository.service.StatusService;
import ch.uzh.ifi.feedback.repository.validation.StatusValidator;

@RequestScoped
@Controller(Status.class)
public class StatusController extends RestController<Status>{

	@Inject
	public StatusController(StatusService dbService, StatusValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@Path("/statuses")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Status> GetAll() throws Exception {
		return super.GetAll();
	}
	
	@Path("/feedbacks/{id}/statuses")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Status> GetByFeedbackId(@PathParam("id")Integer id) throws Exception {
		return super.GetAllFor("feedback_id", id);
	}
	
	@Path("/statuses")
	@POST
	public Status InsertStatus(Status status) throws Exception {
		return super.Insert(status);
	}

	@Path("/statuses/{id}")
	@DELETE
	@Authenticate(UserAuthenticationService.class)
	public void DeleteStatus(@PathParam("id")Integer id) throws Exception 
	{
		super.Delete(id);
	}
	
	@Path("/statuses/{id}")
	@PUT
	@Authenticate(UserAuthenticationService.class)
	public void UpdateStatus(Status status) throws Exception 
	{
		super.Update(status);
	}
}
