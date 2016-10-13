package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.Status;

@Singleton
public class StatusService extends ServiceBase<Status> {

	private Provider<Integer> applicationProvider;
	private DatabaseConfiguration config;

	@Inject
	public StatusService(
			StatusResultParser resultParser,
			DatabaseConfiguration config,
			@Named("application") Provider<Integer> applicationProvider) 
	{
		super(resultParser, Status.class, "feedback_states", config.getRepositoryDb());
		this.config = config;
		this.applicationProvider = applicationProvider;
	}
	
	@Override
	protected List<Status> filterByApplication(List<Status> objects, Connection con) throws SQLException
	{
		if(applicationProvider.get() == null)
			return objects;
		
		List<Status> result = new ArrayList<>();
		for (Status status : objects)
		{
			if(validateByApplication(status, con))
				result.add(status);
		}
		
		return result;
	}
	
	@Override
	protected boolean validateByApplication(Status status, Connection con) throws SQLException 
	{
		Integer appId = applicationProvider.get();
		if(appId == null)
			return true;
		
		int feedbackId = status.getFeedbackId();
		PreparedStatement stmt = con.prepareStatement("Select * FROM "+ config.getRepositoryDb() + ".feedbacks WHERE id = ? AND application_id = ?;");
		stmt.setInt(1, feedbackId);
		stmt.setInt(2, appId);
		ResultSet rs = stmt.executeQuery();
		if(rs.next())
			return true;
		
		return false;
	}
}
