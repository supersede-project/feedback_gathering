package ch.uzh.ifi.feedback.repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.Statement;

import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

public class FeedbackService {
	
	private TransactionManager transactionManager;
	private IDbResultParser<List<Feedback>> dbResultParser;
	
	public FeedbackService(TransactionManager transactionManager, IDbResultParser<List<Feedback>> dbResultParser)
	{
		this.transactionManager = transactionManager;
		this.dbResultParser = dbResultParser;
	}
	
	
	public void SaveFeedback(Feedback feedback) throws Exception {
		transactionManager.withTransaction((con) -> 
		{
			ExecuteTransaction(con, feedback);
		});
	}
	
	public List<Feedback> GetAll(String application) throws Exception {
		
		transactionManager.withTransaction((con) -> {
			
		    ResultSet result = ExecuteTransaction(con, application);
		    dbResultParser.Parse(result);
		    
		});
		
		return dbResultParser.GetResult();
	}
	
	public ResultSet ExecuteTransaction(Connection con, String application) throws IOException, SQLException
	{
	    PreparedStatement s = con.prepareStatement(
	    		
	    		 "Select feedback.id as feedback_id, feedback.title as feedback_title, screenshot.name as screenshot_name, screenshot.url as screenshot_url,"
	    		 + "screenshot.size as screenshot_size, created, lastUpdated, configVersion, text, application_id, user_id, rating.rating, rating.title as rating_title "
	    	   + "FROM feedback_repository.feedback as feedback "
	    	     + "LEFT JOIN feedback_repository.rating_feedback as rating ON rating.feedback_id = feedback.id "
	    	     + "LEFT JOIN feedback_repository.screenshot_feedback as screenshot ON screenshot.feedback_id = feedback.id "
	    	   + "WHERE application_id = ?");
	    
	    s.setString(1, application);
	    ResultSet result = s.executeQuery();
	    
	    return result;
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
	    
	    for(Screenshot screenshot : feedback.getScreenshots()){
	    	
	    	PreparedStatement s2 = con.prepareStatement(
		    		
		    		"INSERT INTO feedback_repository.screenshot_feedback(feedback_id, url, size, name) "
		    		+ "VALUES(?, ?, ?, ?)");
	    	
		    s2.setInt(1, keys.getInt(1));
		    s2.setString(2, screenshot.getPath().toString());
		    s2.setInt(3, screenshot.getSize());
		    s2.setString(4, screenshot.getFileName());
		    s2.execute();
	    }
	}

}
