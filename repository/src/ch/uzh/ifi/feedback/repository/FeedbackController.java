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
public class FeedbackController extends RestController<List<Feedback>> {

    public FeedbackController() {
    }
	
	@Override
	public List<Feedback> Get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String application = (String)request.getAttribute("Application");
		
    	IDbResultParser<List<Feedback>> parser = new FeedbackParser();
    	
		TransactionManager.withTransaction((con) -> {
			
		    PreparedStatement s = con.prepareStatement(
		    		
		    		"Select * FROM feedback_repository.feedback WHERE application_id = ? ");
		    
		    s.setString(1, application);
		    ResultSet result = s.executeQuery();
		    
		    parser.Parse(result);
		    	    	
		});

		return parser.GetResult();
	}
	
	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response, List<Feedback> obj) throws Exception {
		
		for(Feedback feedback : obj){
		
			TransactionManager.withTransaction((con) -> {
				
			    PreparedStatement s = con.prepareStatement(
			    		
			    		"INSERT INTO feedback_repository.feedback (title, created, lastUpdated, configVersion, text, application_id, user_id) "
			    		+ "VALUES(?, NULL, NULL, ?, ?, ? ,?)");
			    
			    s.setString(1, feedback.getTitle());
			    s.setDouble(2, feedback.getConfigVersion());
			    s.setString(3, feedback.getText());
			    s.setString(4, feedback.getApplication());
			    s.setString(5, feedback.getUser());
			    
			    s.execute();
			});
			
		}
	}

}
