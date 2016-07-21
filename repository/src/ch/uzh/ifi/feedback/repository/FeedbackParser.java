package ch.uzh.ifi.feedback.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;

public class FeedbackParser implements IDbResultParser<List<Feedback>> {

	private List<Feedback> result = new ArrayList<>();
	
	@Override
	public void Parse(ResultSet resultSet) throws SQLException {
		
		Feedback currentFeedback = null;
		int currentFeedbackId = -1;
		
		while(resultSet.next())
		{
			int feedbackId = resultSet.getInt("feedback_id"); 
			if(currentFeedbackId != feedbackId)
			{
				if(currentFeedback != null)
					result.add(currentFeedback);
				
				currentFeedbackId = feedbackId;
				currentFeedback = new Feedback();
				currentFeedback.setApplication(resultSet.getString("application_id"));
				currentFeedback.setText(resultSet.getString("text"));
				currentFeedback.setTitle(resultSet.getString("feedback_title"));
				currentFeedback.setCreated(resultSet.getTimestamp("created"));
				currentFeedback.setLastUpdated(resultSet.getTimestamp("lastUpdated"));
				currentFeedback.setUser(resultSet.getString("user_id"));
				currentFeedback.setConfigVersion(Double.parseDouble(resultSet.getString("configVersion")));
			}
			
			if(resultSet.getString("rating_title") != null)
			{
				Rating rating = new Rating();
				rating.setTitle(resultSet.getString("rating_title"));
				rating.setRating(resultSet.getInt("rating"));
				currentFeedback.getRatings().add(rating);
			}
			
			if(resultSet.getString("screenshot_name") != null)
			{
				Screenshot screenshot = new Screenshot();
				screenshot.setFileName(resultSet.getString("screenshot_name"));
				screenshot.setPath(resultSet.getString("screenshot_url"));
				screenshot.setSize(resultSet.getInt("screenshot_size"));
				currentFeedback.getScreenshots().add(screenshot);
			}
		}
		result.add(currentFeedback);
	}

	@Override
	public List<Feedback> GetResult() {
		return result;
	}

}
