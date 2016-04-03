package ch.uzh.ifi.feedback.repository;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.Controller;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

@Controller
(Route = "/{Application}/feedback")
public class FeedbackController extends RestController<List<TextFeedback>> {

    public FeedbackController() {
    }
	
	@Override
	public List<TextFeedback> Get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String application = (String)request.getAttribute("Application");
		
    	IDbResultParser<List<TextFeedback>> parser = new FeedbackParser();
    	
		TransactionManager.withTransaction((con) -> {
			
		    PreparedStatement s = con.prepareStatement(
		    		
		    		"Select * FROM feedback_repository.text_feedback WHERE application = ? ");
		    
		    s.setString(1, application);
		    ResultSet result = s.executeQuery();
		    
		    parser.Parse(result);
		    	    	
		});

		return parser.GetResult();
	}
	
	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response, List<TextFeedback> obj) throws Exception {
		
		TextFeedback feedback = obj.get(0);
		
		TransactionManager.withTransaction((con) -> {
			
		    PreparedStatement s = con.prepareStatement(
		    		
		    		"INSERT INTO feedback_repository.text_feedback (title, text, application) VALUES(?, ?, ?)");
		    
		    s.setString(1, feedback.getTitle());
		    s.setString(2, feedback.getText());
		    s.setString(3, feedback.getApplication());
		    
		    s.execute();
		});
		
	}

}
