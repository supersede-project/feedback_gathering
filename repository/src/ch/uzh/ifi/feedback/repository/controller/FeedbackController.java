package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;
import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.IRequestContext;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.serialization.FeedbackSerializationService;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;

@Controller(Feedback.class)
public class FeedbackController extends RestController<Feedback>{

	@Inject
	public FeedbackController(FeedbackService dbService, IRequestContext requestContext) {
		super(dbService, null, requestContext);
	}
	
	@Path("/feedbacks")
	@GET
	public List<Feedback> GetAll() throws Exception {
		return super.GetAll();
	}
	
	@Path("/applications/{appId}/feedbacks")
	@GET
	public List<Feedback> GetAllForApplication(@PathParam("appId")Integer applicationId) throws Exception {
		return super.GetAllFor("application_id", applicationId);
	}
	
	@Path("/feedbacks/{id}")
	@GET
	public Feedback GetByFeedbackId(@PathParam("id")Integer id) throws Exception {
		return super.GetById(id);
	}
	
	@Path("/feedbacks")
	@POST
	public Feedback InsertFeedback(Feedback feedback) throws Exception {
		return super.Insert(feedback);
	}
	
	@Path("/feedbacks/{id}")
	@DELETE
	public void DeleteFeedback(@PathParam("id")Integer id) throws Exception 
	{
		super.Delete(id);
	}
}
