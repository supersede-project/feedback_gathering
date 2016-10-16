package ch.uzh.ifi.feedback.library.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.rest.validation.IValidator;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

import static java.util.Arrays.asList;


public abstract class RestController<T extends IDbItem<T>> {

	protected IDbService<T> dbService;
	protected IValidator<T> validator;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	private Gson gson; 
	private int createdObjectId;
	
	public RestController(IDbService<T> dbService, IValidator<T> validator, HttpServletRequest request, HttpServletResponse response)
	{
		this.dbService = dbService;
		this.validator = validator;
		this.request = request;
		this.response = response;
		this.gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
	}

	public T GetById(int id) throws Exception {
		return dbService.GetById(id);
	}
	
	public List<T> GetAll() throws Exception {
		return dbService.GetAll();
	}
	
	public List<T> GetAllFor(String foreignKeyName, int foreignKey) throws Exception
	{
		return dbService.GetWhere(asList(foreignKey), foreignKeyName + " = ?");
	}
	
	public T Insert(T object) throws Exception
	{
		Validate(object, false);
		TransactionManager.withTransaction((con) -> {
			createdObjectId = dbService.Insert(con, object);
		});
		
		return GetById(createdObjectId);
	}
	
	public T Update(T object) throws Exception
	{
		
		Validate(object, true);
		TransactionManager.withTransaction((con) -> {
			dbService.Update(con, object);
		});
		
		return GetById(object.getId());
	}
	
	public void Delete(int id) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.Delete(con, id);
		});
	}
	
	protected void Validate(T object, boolean merge) throws Exception
	{
		if (validator != null)
		{
			if(merge)
				object = validator.Merge(object);
			
			ValidationResult result = validator.Validate(object);
			if (result.hasErrors())
			{
				String json = gson.toJson(result);
				throw new ValidationException(json);
			}
				
		}
	}
}
