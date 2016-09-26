package ch.uzh.ifi.feedback.repository.service;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.ContextInformation;

public class ContextInformationResultParser extends DbResultParser<ContextInformation> {

	public ContextInformationResultParser() {
		super(ContextInformation.class);
	}

}
