package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.Application;

@Singleton
public class ApplicationResultParser extends DbResultParser<Application> {

	public ApplicationResultParser() {
		super(Application.class);
	}
}
