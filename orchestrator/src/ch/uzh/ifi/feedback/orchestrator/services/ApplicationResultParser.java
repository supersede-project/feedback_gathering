package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.Application;

public class ApplicationResultParser extends DbResultParser {

	public ApplicationResultParser() {
		super(Application.class);
	}
}
