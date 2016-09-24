package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.sql.Timestamp;

import ch.uzh.ifi.feedback.library.rest.IRequestContext;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import ch.uzh.ifi.feedback.library.rest.validation.IValidator;
import ch.uzh.ifi.feedback.orchestrator.services.IOrchestratorService;

public class OrchestratorController<T extends IDbItem<T>> extends RestController<T> {

	private IOrchestratorService<T> service;
	public OrchestratorController(IOrchestratorService<T> dbService, IValidator<T> validator, IRequestContext requestContext) {
		super(dbService, validator, requestContext);
		
		this.service = dbService;
	}
	
	public T GetByIdAndTime(int id, Timestamp time) throws Exception
	{
		service.setTimestamp(time);
		return super.GetById(id);
	}
}
