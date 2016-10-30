package ch.uzh.ifi.feedback.orchestrator.services;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;

@Singleton
public class MonitorToolResultParser extends DbResultParser<MonitorTool>{

	public MonitorToolResultParser() {
		super(MonitorTool.class);
	}
	
}
