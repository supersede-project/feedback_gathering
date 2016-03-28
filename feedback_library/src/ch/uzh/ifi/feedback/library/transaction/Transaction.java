package ch.uzh.ifi.feedback.library.transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface Transaction {
	 void execute(Connection connection) throws IOException, SQLException;
}
