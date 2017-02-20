package ch.uzh.ifi.feedback.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import ch.uzh.ifi.feedback.library.mail.MailClient;
import ch.uzh.ifi.feedback.library.mail.MailConfiguration;
import ch.uzh.ifi.feedback.repository.mail.MailService;
import ch.uzh.ifi.feedback.repository.mail.RepositoryMailConfiguration;
import ch.uzh.ifi.feedback.repository.model.CategoryFeedback;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;
import ch.uzh.ifi.feedback.repository.model.ScreenshotFeedback;
import ch.uzh.ifi.feedback.repository.model.TextFeedback;

public class MailServiceTest {

	private MailService mailService;

	@Ignore
    @Test
    public void testNotifyOfFeedback() {
    	MailConfiguration mailConfiguration = new RepositoryMailConfiguration();
    	MailClient mailClient = new MailClient(mailConfiguration);
    	mailService = new MailService(mailClient);
    	
    	List<TextFeedback> textFeedbacks = new ArrayList<TextFeedback>();
    	textFeedbacks.add(new TextFeedback(null, null, "Hello, this is my feedback", 1));
    	textFeedbacks.add(new TextFeedback(null, null, "ronnieschaniel@gmail.com", 2));
    	
    	List<RatingFeedback> ratingFeedbacks = new ArrayList<RatingFeedback>();
    	ratingFeedbacks.add(new RatingFeedback(null, "Please rate your experience on this site", 3, null, 9));
    	ratingFeedbacks.add(new RatingFeedback(null, "Please rate another thing", 5, null, 9));
    		
    	List<CategoryFeedback> categoryFeedbacks = new ArrayList<CategoryFeedback>();
    	categoryFeedbacks.add(new CategoryFeedback(null, null, 99, ""));
    	categoryFeedbacks.add(new CategoryFeedback(null, null, 98, null));
    	categoryFeedbacks.add(new CategoryFeedback(null, null, null, "my own category"));
    			
    	String testPath = "test/test_file.pdf";
    	List<ScreenshotFeedback> screenshotFeedbacks = new ArrayList<ScreenshotFeedback>();
    	screenshotFeedbacks.add(new ScreenshotFeedback(null, null, testPath, 18290, "test_file", 55, null, "pdf"));
    	
    	Feedback feedback = new Feedback("Feedback title", 9999, 1, "u192039102", 
    			new Timestamp(123123123), null, "de", null, textFeedbacks, ratingFeedbacks, screenshotFeedbacks, null, null, null, categoryFeedbacks);
    	
    	mailService.NotifyOfFeedback(1, feedback, "ronnieschaniel@gmail.com");
    }
}
