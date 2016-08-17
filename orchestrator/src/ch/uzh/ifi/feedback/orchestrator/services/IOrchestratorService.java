package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.Service.IDbService;

public interface IOrchestratorService<T> extends IDbService<T> {
	
	void setTimestamp(Timestamp timestamp);
	Timestamp getTimestamp();
}
