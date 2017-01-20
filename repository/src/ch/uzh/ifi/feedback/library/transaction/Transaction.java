package ch.uzh.ifi.feedback.library.transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javassist.NotFoundException;

@FunctionalInterface
public interface Transaction {
	 void execute(Connection connection) throws SQLException, NotFoundException;
}
