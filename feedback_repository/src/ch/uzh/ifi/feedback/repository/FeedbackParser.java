package ch.uzh.ifi.feedback.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;

public class FeedbackParser implements IDbResultParser<List<TextFeedback>> {

	private List<TextFeedback> result = new ArrayList<>();
	
	@Override
	public void Parse(ResultSet resultSet) throws SQLException {
		
		while(resultSet.next())
		{
			TextFeedback feedback = new TextFeedback();
			feedback.setApplication(resultSet.getString("application"));
			feedback.setText(resultSet.getString("text"));
			feedback.setTitle(resultSet.getString("title"));
			result.add(feedback);
		}
	}

	@Override
	public List<TextFeedback> GetResult() {
		// TODO Auto-generated method stub
		return result;
	}

}
