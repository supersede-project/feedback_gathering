package ch.uzh.ifi.feedback.library.rest;

import java.util.List;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;


public abstract class RestController<T> {

	private ISerializationService<T> serializationService;
	private IDbService<T> dbService;
	
	public RestController(ISerializationService<T> serializationService, IDbService<T> dbService)
	{
		this.dbService = dbService;
		this.serializationService = serializationService;
	}

	public T GetById(int id) throws Exception {
		return dbService.GetById(TransactionManager.createDatabaseConnection(), id);
	}
	
	public List<T> GetAll() throws Exception {
		return dbService.GetAll(TransactionManager.createDatabaseConnection());
	}
	
	public List<T> GetAllFor(String foreignKeyName, int foreignKey) throws Exception
	{
		return dbService.GetAllFor(TransactionManager.createDatabaseConnection(), foreignKeyName, foreignKey);
	}
	
	public void Insert(T object) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.Insert(con, object);
		});
	}
	
	public void InsertFor(T object, String foreignKeyName, int foreignKey) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.InsertFor(con, object, foreignKeyName, foreignKey);
		});
	}
	
	public void Update(T object) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.Update(con, object);
		});
	}
	
	public void UpdateFor(T object, String foreignKeyName, int foreignKey) throws Exception
	{
		TransactionManager.withTransaction((con) -> {
			dbService.UpdateFor(con, object, foreignKeyName, foreignKey);
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
	
	public ISerializationService<T> getSerializationService() {
		return serializationService;
	}
}
