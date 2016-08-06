package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;

public class GeneralConfigurationResultParser extends DbResultParser {

	public GeneralConfigurationResultParser() {
		super(GeneralConfiguration.class);	}

}
