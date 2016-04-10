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
		
		while(resultSet.next())
		{
			Feedback feedback = new Feedback();
			feedback.setApplication(resultSet.getString("application_id"));
			feedback.setText(resultSet.getString("text"));
			feedback.setTitle(resultSet.getString("title"));
			feedback.setCreated(resultSet.getTimestamp("created"));
			feedback.setLastUpdated(resultSet.getTimestamp("lastUpdated"));
			feedback.setUser(resultSet.getString("user_id"));
			feedback.setConfigVersion(Double.parseDouble(resultSet.getString("configVersion")));
			
			result.add(feedback);
		}
	}

	@Override
	public List<Feedback> GetResult() {
		// TODO Auto-generated method stub
		return result;
	}

}
