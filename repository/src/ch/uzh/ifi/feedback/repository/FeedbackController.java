package ch.uzh.ifi.feedback.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Statement;

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
		    		
		    		 "Select feedback.id as feedback_id, feedback.title as feedback_title, created, lastUpdated, configVersion, text, "
		    	   + "application_id, user_id, rating.rating, rating.title as rating_title  "
		    	   + "FROM feedback_repository.feedback as feedback LEFT JOIN feedback_repository.rating_feedback as rating "
		    	   + "ON rating.feedback_id = feedback.id "
		    	   + "WHERE application_id = ? ");
		    
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
			    		+ "VALUES(?, NULL, NULL, ?, ?, ? ,?)", Statement.RETURN_GENERATED_KEYS);
			    
			    s.setString(1, feedback.getTitle());
			    s.setDouble(2, feedback.getConfigVersion());
			    s.setString(3, feedback.getText());
			    s.setString(4, feedback.getApplication());
			    s.setString(5, feedback.getUser());
			    
			    s.execute();
			    
			    ResultSet keys = s.getGeneratedKeys();
			    keys.next();
			    
			    for(Rating rating : feedback.getRatings()){
			    	
			    	PreparedStatement s2 = con.prepareStatement(
				    		
				    		"INSERT INTO feedback_repository.rating_feedback(title, rating, feedback_id) "
				    		+ "VALUES(?, ?, ?)");
			    	
				    s2.setString(1, rating.getTitle());
				    s2.setInt(2, rating.getRating());
				    s2.setInt(3, keys.getInt(1));
				    s2.execute();
			    }
			});
			
		}
	}

}
