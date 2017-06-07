package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.GeneralConfiguration;

@Singleton
public class GeneralConfigurationResultParser extends DbResultParser<GeneralConfiguration> {

	public GeneralConfigurationResultParser() {
		super(GeneralConfiguration.class);	}

}
