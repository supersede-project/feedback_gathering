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

/**
 * This class is the base class for all controller instances that access the database via the IDbService<T> interface.
 * The class provides basic CRUD functionality including validation and transaction management for insert, update and delete requests.
 * For accessing of the database, the IDbService<T> interface is used.
 * 
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 * @param <T> the model class that is processed by this controller
 */
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

	
	/**
	 * Retrieves and returns an instance of an IDbItem<T> by its id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T GetById(int id) throws Exception {
		
		return dbService.GetById(id);
	}
	
	/**
	 * Retrieves and returns all instances of IDbItem<T>
	 * @return
	 * @throws Exception
	 */
	public List<T> GetAll() throws Exception {
		return dbService.GetAll();
	}
	
	/**
	 * Retrieves and returns all instances of IDbItem<T> that match a specific foreign key
	 * @param foreignKeyName the name of the foreign key
	 * @param foreignKey the value of the foreign key
	 * @return
	 * @throws Exception
	 */
	public List<T> GetAllFor(String foreignKeyName, int foreignKey) throws Exception
	{
		return dbService.GetWhere(asList(foreignKey), foreignKeyName + " = ?");
	}
	
	/**
	 * Inserts and returns an object of IDbItem<T>
	 * @param object the object to insert
	 * @return
	 * @throws Exception
	 */
	public T Insert(T object) throws Exception
	{
		Validate(object, false);
		TransactionManager.withTransaction((con) -> {
			createdObjectId = dbService.Insert(con, object);
		});
		
		return GetById(createdObjectId);
	}
	
	/**
	 * Updates and returns an object of IDbItem<T>
	 * @param object the object to update
	 * @return
	 * @throws Exception
	 */
	public T Update(T object) throws Exception
	{
		
		Validate(object, true);
		TransactionManager.withTransaction((con) -> {
			dbService.Update(con, object);
		});
		
		return GetById(object.getId());
	}
	
	/**
	 * Deletes an object of IDbItem<T> by its id
	 * @param id the id of the object to delete
	 * @throws Exception
	 */
	public void Delete(int id) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.Delete(con, id);
		});
	}
	
	/**
	 * Validates an object of IDbItem<T>
	 * @param object the object to validate
	 * @param merge indicates whether the object has to merged with the values stored in the database. This is only used for update requests.
	 * @return
	 * @throws Exception
	 */
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
