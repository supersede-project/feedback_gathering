package ch.uzh.ifi.feedback.orchestrator.controllers;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.*;
import ch.uzh.ifi.feedback.library.transaction.*;
public abstract class FeedbackGatheringController<T> implements IRestController<T> {
    
	private ISerializationService<T> serializationService;
	private IDbService<T> dbService;
	private Type serializationType;
	
    public FeedbackGatheringController(IDbService<T> dbService, ISerializationService<T> serializationService) {
		this.dbService = dbService;
		this.serializationService = serializationService;
		
		Type superclass = this.getClass().getGenericSuperclass();
		this.serializationType = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		
	}

    /*
	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response, T object) throws Exception
	{
		int id = dbService.Create(object);
		response.getWriter().append(serializationService.Serialize(dbService.Read(id)));
	}

	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response, T object) throws Exception
	{
		int id = dbService.Update(object);
		response.getWriter().append(serializationService.Serialize(dbService.Read(id)));
	}
	*/
    
	@Override
	public ISerializationService<T> getSerializationService() {
		return serializationService;
	}
	
	@Override
	public Type getSerializationType()
	{
		return this.serializationType;
	}
	
	protected IDbService<T> getDbService(){
		return dbService;
	}
}
