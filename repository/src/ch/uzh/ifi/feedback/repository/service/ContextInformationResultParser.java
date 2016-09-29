package ch.uzh.ifi.feedback.repository.service;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.repository.model.ContextInformation;

@Singleton
public class ContextInformationResultParser extends DbResultParser<ContextInformation> {

	public ContextInformationResultParser() {
		super(ContextInformation.class);
	}

}
