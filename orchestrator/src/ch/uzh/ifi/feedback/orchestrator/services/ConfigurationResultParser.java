package ch.uzh.ifi.feedback.orchestrator.services;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;

public class ConfigurationResultParser extends DbResultParser<Configuration> {

	public ConfigurationResultParser() {
		super(Configuration.class);
	}

}
