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
(Route = "/{Application}/feedbacks")
public class FeedbacksController extends RestController<List<Feedback>> {

	public FeedbacksController(TransactionManager transactionManager) {
		super(transactionManager);
	}

	@Override
	public List<Feedback> Get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String application = (String)request.getAttribute("Application");
		
    	IDbResultParser<List<Feedback>> parser = new FeedbackParser();
    	
		getTransactionManager().withTransaction((con) -> {
			
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
}
