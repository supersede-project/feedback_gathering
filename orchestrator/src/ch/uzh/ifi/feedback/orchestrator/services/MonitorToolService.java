package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Timestamp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.transaction.OrchestratorDatabaseConfiguration;

public class MonitorToolService extends OrchestratorService<MonitorTool> {

	@Inject
	public MonitorToolService(
			MonitorToolResultParser resultParser, 
			OrchestratorDatabaseConfiguration config,
			@Named("timestamp")Provider<Timestamp> timestampProvider) {
		
		super(resultParser, MonitorTool.class, "monitor_tool", config.getDatabase(), timestampProvider);
		
	}
	
}
