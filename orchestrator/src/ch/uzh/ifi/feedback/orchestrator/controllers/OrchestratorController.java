package ch.uzh.ifi.feedback.orchestrator.controllers;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.validation.IValidator;
import ch.uzh.ifi.feedback.orchestrator.services.IOrchestratorService;

public class OrchestratorController<T extends IDbItem<T>> extends RestController<T> {

	private IOrchestratorService<T> service;
	public OrchestratorController(IOrchestratorService<T> dbService, IValidator<T> validator, HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
		
		this.service = dbService;
	}
	
	public T GetByIdAndTime(int id, Timestamp time) throws Exception
	{
		service.setTimestamp(time);
		return super.GetById(id);
	}
}
