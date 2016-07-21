package ch.uzh.ifi.feedback.repository;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.Controller;
import ch.uzh.ifi.feedback.library.rest.DefaultSerializer;
import ch.uzh.ifi.feedback.library.rest.ISerializationService;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

@Controller
(Route = "/{Application}/feedbacks")
public class FeedbacksController extends RestController<List<Feedback>>{

	private ISerializationService<List<Feedback>> serializationService;
	private FeedbackService feedbackService;
	
	public FeedbacksController() {

		this.feedbackService = new FeedbackService(new TransactionManager(), new FeedbackParser());
		serializationService = new DefaultSerializer<>(getSerializationType());
	}

	@Override
	public List<Feedback> Get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String application = (String)request.getAttribute("Application");
		return feedbackService.GetAll(application);
	}

	@Override
	public ISerializationService<List<Feedback>> getSerializationService() {
		return serializationService;
	}
}
