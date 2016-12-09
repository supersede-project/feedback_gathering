package ch.uzh.ifi.feedback.repository.controller;

import java.util.ArrayList;
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
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.Status;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.service.StatusOptionService;
import ch.uzh.ifi.feedback.repository.service.StatusService;
import ch.uzh.ifi.feedback.repository.validation.StatusValidator;
import javassist.NotFoundException;

import static java.util.Arrays.asList;

import java.sql.SQLException;

@Controller(Status.class)
@RequestScoped
public class FeedbackStatusController extends RestController<Status>{

	private StatusOptionService optionService;
	private FeedbackService feedbackService;

	@Inject
	public FeedbackStatusController(
			StatusService dbService,
			FeedbackService feedbackService,
			StatusOptionService optionService,
			StatusValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response) 
	{
		super(dbService, validator, request, response);
		
		this.feedbackService = feedbackService;
		this.optionService = optionService;
	}
	
	@Path("/{lang}/applications/{application_id}/states")
	@GET
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public List<Status> GetGeneralStatesByApplication(@PathParam("application_id")Integer applicationId) throws Exception 
	{
		List<Feedback> feedbacks = feedbackService.GetWhere(asList(applicationId), "application_id = ?");
		List<Status> result = new ArrayList<Status>();
		for(Feedback f : feedbacks)
		{
			List<Status> states = dbService.GetWhere(asList(f.getId(), null), "feedback_id = ?", "api_user_id is ?");
			if(!states.isEmpty())
				result.add(states.get(0));
		}

		return result;
	}
	
	@Path("/{lang}/applications/{application_id}/api_users/{user_id}/states")
	@GET
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public List<Status> GetAllStatesByApplicationAndUser(
			@PathParam("application_id")Integer applicationId,
			@PathParam("user_id")Integer userId) throws Exception 
	{
		List<Feedback> feedbacks = feedbackService.GetWhere(asList(applicationId), "application_id = ?");
		List<Status> result = new ArrayList<Status>();
		for(Feedback f : feedbacks)
		{
			List<Status> states = dbService.GetWhere(asList(f.getId(), userId), "feedback_id = ?", "api_user_id = ?");
			if(!states.isEmpty())
				result.add(states.get(0));
		}

		return result;
	}
	
	@Path("/{lang}/applications/{application_id}/feedbacks/{id}/status")
	@GET
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public Status GetByFeedbackId(
			@PathParam("id")Integer id,
			@PathParam("application_id")Integer applicationId) throws Exception 
	{
		List<Status> states = dbService.GetWhere(asList(id, null), "feedback_id = ?", "api_user_id is ?");
		if(states.size() > 0)
		{
			if(!validateApplication(states.get(0), applicationId))
				throw new NotFoundException("The requested status does not exist for the provided application");
			
			return states.get(0);
		}
			
		throw new NotFoundException("feedback with id '" + id + "' does not exist !");
	}
	
	@Path("/{lang}/applications/{application_id}/feedbacks/{feedback_id}/api_users/{user_id}/status")
	@GET
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public Status GetByFeedbackAndUserId(
			@PathParam("feedback_id")Integer feedbackId, 
			@PathParam("user_id") Integer userId,
			@PathParam("application_id")Integer applicationId) throws Exception 
	{
		List<Status> states = dbService.GetWhere(asList(feedbackId, userId), "feedback_id = ?", "api_user_id = ?");
		if(states.size() > 0)
		{
			if(!validateApplication(states.get(0), applicationId))
				throw new NotFoundException("The requested status does not exist for the provided application");
			
			return states.get(0);
		}

		throw new NotFoundException("feedback with id '" + feedbackId + "' has no status for user '"+ userId  + " '!");
	}

	@Path("/{lang}/applications/{application_id}/states/{id}")
	@DELETE
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public void DeleteStatus(
			@PathParam("id")Integer id,
			@PathParam("application_id")Integer applicationId) throws Exception 
	{
		Status status = super.GetById(id);
		if(!validateApplication(status, applicationId))
			throw new NotFoundException("The status does not exist for the provided application");
		
		super.Delete(id);
	}
	
	@Path("/{lang}/applications/{application_id}/states")
	@PUT
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public Status UpdateStatus(@PathParam("application_id")Integer applicationId, Status status) throws Exception 
	{
		Status oldStatus = dbService.GetById(status.getId());
		
		if(!validateApplication(oldStatus, applicationId))
			throw new NotFoundException("The status does not exist for the provided application");	
		
		//validate order
		boolean isUserSpecific = oldStatus.getApiUserId() != null;
		StatusOption oldOption = optionService.GetWhere(asList(oldStatus.getStatus(), isUserSpecific), "`name` = ?", "user_specific = ?").get(0);
		List<StatusOption> newOptions = optionService.GetWhere(asList(status.getStatus(), isUserSpecific), "`name` = ?", "user_specific = ?");
		
		if(newOptions.size() == 0)
			throw new NotFoundException("Status with name " + status.getStatus() + " does not exist!");
		
		StatusOption newOption = newOptions.get(0);
		if(newOption.getOrder() != oldOption.getOrder() + 1)
			throw new ValidationException("Can not set status from " + oldStatus.getStatus() + " to " + status.getStatus() + ". Status order is violated!");
		
		return super.Update(status);
	}
	
	private boolean validateApplication(Status status, Integer applicationId)
	{
		Feedback feedback = null;
		try {
			feedback = feedbackService.GetById(status.getFeedbackId());
		} catch (SQLException | NotFoundException e) {
			e.printStackTrace();
		}
		
		if(!feedback.getApplicationId().equals(applicationId))
			return false;
		return true;
	}
}
