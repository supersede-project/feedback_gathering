package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;

@Singleton
public class MonitorConfigurationResultParser extends DbResultParser<MonitorConfiguration>{

	public MonitorConfigurationResultParser() {
		super(MonitorConfiguration.class);
	}

}
