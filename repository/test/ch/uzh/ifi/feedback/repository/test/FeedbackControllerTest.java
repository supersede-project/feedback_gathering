package ch.uzh.ifi.feedback.repository.test;

import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import ch.uzh.ifi.feedback.library.transaction.Transaction;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import ch.uzh.ifi.feedback.repository.model.Feedback;
import ch.uzh.ifi.feedback.repository.controller.FeedbackController;
import ch.uzh.ifi.feedback.repository.service.FeedbackResultParser;
import ch.uzh.ifi.feedback.repository.service.FeedbackService;
import ch.uzh.ifi.feedback.repository.model.RatingFeedback;

import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FeedbackControllerTest {

	private TransactionManager transactionManager;
	private Connection connection;
	private PreparedStatement statement1;
	private PreparedStatement statement2;
	private FeedbackService testee;

	@Before
	public void setUp() throws Exception {

		transactionManager = mock(TransactionManager.class);
		connection = mock(Connection.class);
		statement1 = mock(PreparedStatement.class);
		statement2 = mock(PreparedStatement.class);
		ResultSet rs = mock(ResultSet.class);
		when(transactionManager.createDatabaseConnection()).thenReturn(connection);
		when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement1);
		when(connection.prepareStatement(anyString())).thenReturn(statement2);
		when(statement1.getGeneratedKeys()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(1);
	}

	private Feedback getTestFeedback() {
		Feedback feedback = new Feedback();
		feedback.setApplicationId(1);
		feedback.setConfigVersion(1.0);
		List<RatingFeedback> ratings = new ArrayList<>();
		RatingFeedback rating = new RatingFeedback();
		rating.setTitle("rating");
		rating.setRating(4);
		ratings.add(rating);
		feedback.setRatings(ratings);
		feedback.setTitle("Title");
		feedback.setUser("User");
		return feedback;
	}

	@Test
	public void testExecuteTransaction() throws Exception {

		// arrange
		testee = new FeedbackService(transactionManager, mock(FeedbackResultParser.class));
		Feedback feedback = getTestFeedback();

		// act
		testee.ExecuteTransaction(connection, feedback);

		// assert
		verify(connection).prepareStatement(anyString(), anyInt());
		verify(statement1).setString(1, feedback.getTitle());
		verify(statement1).setDouble(2, feedback.getConfigVersion());
		verify(statement1).setLong(1, feedback.getApplicationId());
		verify(statement1).setString(5, feedback.getUser());
		verify(statement1).execute();

		RatingFeedback rating = feedback.getRatingFeedbacks().get(0);
		verify(statement2).setString(1, rating.getTitle());
		verify(statement2).setInt(2, rating.getRating());
		verify(statement2).execute();
	}

}
