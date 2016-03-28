package ch.uzh.ifi.feedback.library.transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDbResultParser<T> {
	
	void Parse(ResultSet resultSet) throws SQLException;
	T GetResult();
}
