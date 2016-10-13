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
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.repository.model.Status;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.service.StatusOptionService;
import ch.uzh.ifi.feedback.repository.service.StatusService;
import ch.uzh.ifi.feedback.repository.validation.StatusValidator;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

@RequestScoped
@Controller(Status.class)
public class StatusController extends RestController<Status>{

	private StatusOptionService optionService;

	@Inject
	public StatusController(
			StatusService dbService,
			StatusOptionService optionService,
			StatusValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
		
		this.optionService = optionService;
	}
	
	@Path("/applications/{application_id}/states")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Status> GetAll() throws Exception {
		return super.GetAll();
	}
	
	@Path("/applications/{application_id}/feedbacks/{id}/status")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public Status GetByFeedbackId(@PathParam("id")Integer id) throws Exception {
		List<Status> states = dbService.GetWhere(asList(id, null), "feedback_id = ?", "api_user_id is ?");
		if(states.size() > 0)
			return states.get(0);
		
		throw new NotFoundException("feedback with id '" + id + "' does not exist !");
	}
	
	@Path("/applications/{application_id}/feedbacks/{feedback_id}/api_users/{user_id}/status")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public Status GetByFeedbackAndUserId(@PathParam("feedback_id")Integer feedbackId, @PathParam("user_id") Integer userId) throws Exception {
		List<Status> states = dbService.GetWhere(asList(feedbackId, userId), "feedback_id = ?", "api_user_id = ?");
		if(states.size() > 0)
			return states.get(0);
		
		throw new NotFoundException("feedback with id '" + feedbackId + "' has no status for user '"+ userId  + " '!");
	}
	
	@Path("/applications/{application_id}/api_users/{id}/states")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Status> GetByApiUser(@PathParam("id")Integer id) throws Exception {
		List<Status> states = dbService.GetWhere(asList(id), "api_user_id = ?");
		return states;
	}

	@Path("/applications/{application_id}/states/{id}")
	@DELETE
	@Authenticate(UserAuthenticationService.class)
	public void DeleteStatus(@PathParam("id")Integer id) throws Exception 
	{
		super.Delete(id);
	}
	
	@Path("/applications/{application_id}/states")
	@PUT
	@Authenticate(UserAuthenticationService.class)
	public Status UpdateStatus(Status status) throws Exception 
	{
		//validate order
		Status oldStatus = dbService.GetById(status.getId());
		StatusOption oldOption = optionService.GetWhere(asList(oldStatus.getStatus()), "`name` = ?").get(0);
		List<StatusOption> newOptions = optionService.GetWhere(asList(status.getStatus()), "`name` = ?");
		
		if(newOptions.size() == 0)
			throw new NotFoundException("Status does not exist!");
		
		StatusOption newOption = newOptions.get(0);
		
		if(newOption.getOrder() != oldOption.getOrder() + 1)
			throw new ValidationException("the status can not be updated to '" + status.getStatus() + "'");
			
		return super.Update(status);
	}
}
