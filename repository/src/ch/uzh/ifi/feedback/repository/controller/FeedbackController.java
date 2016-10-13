package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.validation.FeedbackValidator;
import javassist.NotFoundException;

@RequestScoped
@Controller(Feedback.class)
public class FeedbackController extends RestController<Feedback>{

	@Inject
	public FeedbackController(FeedbackService dbService, FeedbackValidator validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@Path("/applications/{application_id}/feedbacks")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public List<Feedback> GetAllForApplication(@PathParam("application_id")Integer applicationId) throws Exception {
		return super.GetAllFor("application_id", applicationId);
	}
	
	@Path("/applications/{application_id}/feedbacks/{id}")
	@GET
	@Authenticate(UserAuthenticationService.class)
	public Feedback GetByFeedbackId(@PathParam("id")Integer id, @PathParam("application_id")Integer applicationId) throws Exception 
	{
		Feedback feedback = super.GetById(id);
		if(!feedback.getApplicationId().equals(applicationId))
			throw new NotFoundException("object does not exist in given application context");
		
		return feedback;
	}
	
	@Path("/applications/{application_id}/feedbacks")
	@POST
	public Feedback InsertFeedback(Feedback feedback) throws Exception {
		return super.Insert(feedback);
	}
	
	@Path("/applications/{application_id}/feedbacks/{id}")
	@DELETE
	@Authenticate(UserAuthenticationService.class)
	public void DeleteFeedback(@PathParam("id")Integer id, @PathParam("application_id")Integer applicationId) throws Exception 
	{
		Feedback toDelete = super.GetById(id);
		if(!toDelete.getApplicationId().equals(applicationId))
			throw new NotFoundException("object does not exist in given application context");
		
		super.Delete(id);
	}
}
