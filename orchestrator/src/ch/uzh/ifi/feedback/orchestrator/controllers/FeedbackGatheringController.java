package ch.uzh.ifi.feedback.orchestrator.controllers;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.*;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import ch.uzh.ifi.feedback.library.transaction.*;
public abstract class FeedbackGatheringController<T> {
    /*
	private ISerializationService<T> serializationService;
	private IDbService<T> dbService;
	private Type serializationType;
	
    public FeedbackGatheringController(IDbService<T> dbService, ISerializationService<T> serializationService) {
		this.dbService = dbService;
		this.serializationService = serializationService;
		
		Type superclass = this.getClass().getGenericSuperclass();
		this.serializationType = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		
	}

	protected IDbService<T> getDbService(){
		return dbService;
	}*/
}
