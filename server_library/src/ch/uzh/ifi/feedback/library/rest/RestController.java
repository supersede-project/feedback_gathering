package ch.uzh.ifi.feedback.library.rest;

import java.util.List;

import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.validation.IValidator;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

import static java.util.Arrays.asList;


public abstract class RestController<T extends IDbItem<T>> {

	protected IDbService<T> dbService;
	protected IValidator<T> validator;
	
	public RestController(IDbService<T> dbService, IValidator<T> validator)
	{
		this.dbService = dbService;
		this.validator = validator;
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
	
	public void Insert(T object) throws Exception
	{
		validator.Validate(object);
		
		TransactionManager.withTransaction((con) -> {
			dbService.Insert(con, object);
		});
	}
	
	public void Update(T object) throws Exception
	{
		T mergedObject = validator.Merge(object);
		validator.Validate(mergedObject);
		
		TransactionManager.withTransaction((con) -> {
			dbService.Update(con, object);
		});
	}
	
	public void Delete(int id) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.Delete(con, id);
		});
	}
	
	public final void SetLanguage(String lang)
	{
		dbService.SetLanguage(lang);
	}
}
