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
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.authorization.UserAuthenticationService;
import ch.uzh.ifi.feedback.repository.mail.MailService;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.validation.FeedbackValidator;
import javassist.NotFoundException;

@RequestScoped
@Controller(Feedback.class)
public class FeedbackController extends RestController<Feedback>{

	private MailService mailService;
	
	@Inject
	public FeedbackController(
			FeedbackService dbService,
			MailService mailService,
			FeedbackValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		super(dbService, validator, request, response);
		this.mailService = mailService;
	}
	
	@Path("/{lang}/applications/{application_id}/feedbacks")
	@GET
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public List<Feedback> GetAllForApplication(@PathParam("application_id")Integer applicationId) throws Exception {
		return super.GetAllFor("application_id", applicationId);
	}
	
	@Path("/{lang}/applications/{application_id}/feedbacks/{id}")
	@GET
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public Feedback GetByFeedbackId(@PathParam("id")Integer id, @PathParam("application_id")Integer applicationId) throws Exception 
	{
		Feedback feedback = super.GetById(id);
		if(!feedback.getApplicationId().equals(applicationId))
			throw new NotFoundException("object does not exist in given application context");
		
		return feedback;
	}
	
	@Path("/{lang}/applications/{application_id}/feedbacks")
	@POST
	public Feedback InsertFeedback(Feedback feedback) throws Exception {
		Feedback created =  super.Insert(feedback);
		mailService.NotifyOfFeedback(feedback.getApplicationId(), created.getId());
		return created;
	}
	
	@Path("/{lang}/applications/{application_id}/feedbacks/{id}")
	@DELETE
	@Authenticate(service = UserAuthenticationService.class, scope = "APPLICATION")
	public void DeleteFeedback(@PathParam("id")Integer id, @PathParam("application_id")Integer applicationId) throws Exception 
	{
		Feedback toDelete = super.GetById(id);
		if(!toDelete.getApplicationId().equals(applicationId))
			throw new NotFoundException("object does not exist in given application context");
		
		super.Delete(id);
	}
}
