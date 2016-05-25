package ch.uzh.ifi.feedback.repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Statement;

import ch.uzh.ifi.feedback.library.rest.Controller;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.repository.interfaces.FeedbackReceiver;

@Controller
(Route = "/{Application}/feedback")
public class FeedbackController extends RestController<Feedback> implements FeedbackReceiver{
	
	public FeedbackController(TransactionManager transactionManager) {
		super(transactionManager);
	}
	
	public void ExecuteTransaction(Connection con, Feedback feedback) throws IOException, SQLException {
		
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
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, Feedback feedback) throws Exception {
		
		StoreFeedback(feedback);
		response.setStatus(201);
		response.getWriter().append(Serialize(feedback));
	}

	@Override
	public void StoreFeedback(Feedback feedback) throws Exception {
		getTransactionManager().withTransaction((con) -> 
		{
			ExecuteTransaction(con, feedback);
		});
	}
}
