package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Timestamp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.orchestrator.model.MonitorType;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;

public class MonitorTypeService extends OrchestratorService<MonitorType> {

	@Inject
	public MonitorTypeService(
			MonitorTypeResultParser resultParser, 
			OrchestratorDatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) {
		
		super(resultParser, MonitorType.class, "monitor_type", config.getDatabase(), timestampProvider);
		
	}

}
