package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Timestamp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;

public class MonitorConfigurationService extends OrchestratorService<MonitorConfiguration> {

	@Inject
	public MonitorConfigurationService(
			MonitorConfigurationResultParser resultParser,
			OrchestratorDatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) {
		
		super(resultParser, MonitorConfiguration.class, "monitor_configuration", config.getDatabase(), timestampProvider);

	}

}
