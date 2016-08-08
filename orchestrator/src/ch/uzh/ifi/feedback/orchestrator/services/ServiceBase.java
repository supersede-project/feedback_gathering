package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.library.rest.Service.IDbService;
import javassist.NotFoundException;

public abstract class ServiceBase<T> implements IDbService<T> {
	
	private Class<T> serviceClass;
	private String tableName;
	private List<IDbService<?>> childServices;
	private String selectedLanguage;
	
	protected DbResultParser<T> resultParser;
	
	public ServiceBase(DbResultParser<T> resultParser, Class<T> serviceClass, String tableName, IDbService<?>... services)
	{
		this.serviceClass = serviceClass;
		this.tableName = tableName;
		this.resultParser = resultParser;
		this.childServices = new ArrayList<>();
		for(IDbService<?> service : services)
		{
			childServices.add(service);
		}
	}
	
	@Override
	public void SetLanguage(String lang)
	{
		this.selectedLanguage = lang;
		childServices.stream().forEach(s -> s.SetLanguage(lang));
	}
	
	@Override
	public String GetLanguage()
	{
		return selectedLanguage;
	}
	
	@Override
	public T GetById(Connection con, int id) throws SQLException, NotFoundException 
	{
		ResultSet result = CheckId(con, id);
		T instance = null;
		try {
			instance = serviceClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		resultParser.SetFields(instance, result);
		return instance;
	}
	
	@Override
	public List<T> GetAll(Connection con) throws SQLException, NotFoundException
	{
		String statement = String.format("SELECT * FROM feedback_orchestrator.%s ;", tableName);
		PreparedStatement s = con.prepareStatement(statement);
		ResultSet result = s.executeQuery();

		return getList(result);
	}
	
	@Override
	public void Delete(Connection con, int id) throws SQLException, NotFoundException
	{
		CheckId(con, id);
		
		String statement = String.format("DELETE * FROM feedback_orchestrator.%s as t WHERE t.id = ? ;", tableName);
		PreparedStatement s = con.prepareStatement(statement);
		s.setInt(1, id);
		s.execute();
	}
	
	protected List<T> GetAllFor(Connection con, String foreignTableName, String foreignKeyName, int foreignKey)
			throws SQLException, NotFoundException
	{
	    String statement = String.format(
    		    "SELECT * "
    		  + "FROM feedback_orchestrator.%s as f "
    		  + "JOIN feedback_orchestrator.%s as t ON t.%s = f.id "
    		  + "WHERE f.id = ? ;", foreignTableName, tableName, foreignKeyName);
	    
	    PreparedStatement s = con.prepareStatement(statement);
	    s.setInt(1, foreignKey);
	    ResultSet result = s.executeQuery();
	    
	    return getList(result);
	}
	
	protected ResultSet CheckId(Connection con, int id) throws SQLException, NotFoundException
	{
		String statement = String.format("SELECT * FROM feedback_orchestrator.%s as t WHERE t.id = ? ;", tableName);
		PreparedStatement s = con.prepareStatement(statement);
		s.setInt(1, id);
		
		ResultSet result = s.executeQuery();
		
		if(!result.next())
			throw new NotFoundException("Table '" + tableName + "' does not contain an object with id " + id);
		
		return result;
	}
	
	private List<T> getList(ResultSet result) throws SQLException
	{
		List<T> list = new ArrayList<>();
		while(result.next())
		{
			try {
				T instance = serviceClass.newInstance();
				resultParser.SetFields(instance, result);
				list.add(instance);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
}
