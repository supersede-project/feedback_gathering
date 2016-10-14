package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.Status;

@Singleton
public class StatusResultParser extends DbResultParser<Status> {

	public StatusResultParser() {
		super(Status.class);
	}

}
