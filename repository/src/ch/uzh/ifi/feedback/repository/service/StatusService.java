package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.Status;

@Singleton
public class StatusService extends ServiceBase<Status> {

	@Inject
	public StatusService(
			StatusResultParser resultParser,
			DatabaseConfiguration config) 
	{
		super(resultParser, Status.class, "feedback_states", config.getRepositoryDb());
	}
}
