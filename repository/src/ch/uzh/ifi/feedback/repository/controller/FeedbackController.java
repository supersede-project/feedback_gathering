package ch.uzh.ifi.feedback.repository.controller;

import java.util.List;
import java.util.concurrent.*;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import ch.uzh.ifi.feedback.repository.integration.DataProviderIntegratorRepository;
import ch.uzh.ifi.feedback.repository.mail.MailService;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.validation.FeedbackValidator;
import javassist.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

@RequestScoped
@Controller(Feedback.class)
public class FeedbackController extends RestController<Feedback>{

	private static final Log LOGGER = LogFactory.getLog(FeedbackController.class);
	private MailService mailService;
	private DataProviderIntegratorRepository dataProviderIntegratorRepository;
	private Gson gson;
	private final int corePoolSize = 20;
	private ScheduledThreadPoolExecutor executor;
	
	@Inject
	public FeedbackController(
			FeedbackService dbService,
			MailService mailService,
			FeedbackValidator validator, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		super(dbService, validator, request, response);
		this.mailService = mailService;
		this.dataProviderIntegratorRepository = new DataProviderIntegratorRepository();
		this.gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		resetScheduler();
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

		executor = new ScheduledThreadPoolExecutor(corePoolSize);
		executor.execute(new AsyncEmailNotification(created));
		executor.execute(new AsyncDataProviderIntegrator(created));
		
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

	private class AsyncEmailNotification implements Runnable {
		Feedback feedback;

		public AsyncEmailNotification(Feedback feedback) {
			this.feedback = feedback;
		}

		public void run() {
			try {
				mailService.NotifyOfFeedback(feedback.getApplicationId(), feedback, null, null);
				resetScheduler();
			} catch (Exception e) {
				resetScheduler();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Async task for the communication with WP2.
	 */
	private class AsyncDataProviderIntegrator implements Runnable {
		Feedback feedback;

		public AsyncDataProviderIntegrator(Feedback feedback) {
			this.feedback = feedback;
		}

		public void run() {
			LOGGER.debug("WP2 communication");
			try {
				String topicIdFeedback = "5ff7d393-e2a5-49fd-a4de-f4e1f7480bf4";
				String json = gson.toJson(feedback);
				JSONObject jsonData = new JSONObject(json);
				dataProviderIntegratorRepository.ingestJsonData(jsonData, topicIdFeedback);
				resetScheduler();
			} catch (Exception e) {
				resetScheduler();
				e.printStackTrace();
			}
		}
	}

	private void resetScheduler() {
		if (executor != null && !executor.isTerminating()) {
			// Cancel scheduled but not started task, and avoid new ones
			executor.shutdown();

			// Wait for the running tasks
			try {
				executor.awaitTermination(20, TimeUnit.SECONDS);
			} catch (InterruptedException ex) {
				LOGGER.warn("Interupt during awating termination", ex);
			}

			// Interrupt the threads and shutdown the scheduler
			executor.shutdownNow();
		}
	}
}
