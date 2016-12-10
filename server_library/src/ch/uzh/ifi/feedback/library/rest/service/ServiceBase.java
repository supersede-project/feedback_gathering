package ch.uzh.ifi.feedback.library.rest.service;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.uzh.ifi.feedback.library.rest.annotations.DbIgnore;
import ch.uzh.ifi.feedback.library.rest.annotations.Id;
import ch.uzh.ifi.feedback.library.rest.service.IDbService;
import ch.uzh.ifi.feedback.library.transaction.DbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

/**
 * This class is the base class for all services that access the database. It provides basic CRUD functionality.
 * The queries are generated based on field metadata retrieved from the model classes.
 * 
 * @param <T> the model class that is processed by this instance
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public abstract class ServiceBase<T extends IDbItem> implements IDbService<T> {
	
	protected String tableName;
	protected String dbName;
	protected Class<T> serviceClass;
	
	protected DbResultParser<T> resultParser;
	
	public ServiceBase(
			DbResultParser<T> resultParser, 
			Class<T> serviceClass, 
			String tableName,
			String dbName)
	{
		this.serviceClass = serviceClass;
		this.tableName = tableName;
		this.resultParser = resultParser;
		this.dbName = dbName;
	}
	
	/**
	 * This method retrieves an instance of IDbItem<T> from the database based on its id.
	 * @param id the id of the object to retrieve
	 * @return
	 */
	@Override
	public T GetById(int id) throws SQLException, NotFoundException
	{
		Connection con = TransactionManager.createDatabaseConnection();
		
		try{
			String statement = String.format("SELECT * FROM %s.%s as t WHERE t.id = ? ;", dbName, tableName);
			PreparedStatement s = con.prepareStatement(statement);
			s.setInt(1, id);
			ResultSet result = s.executeQuery();
			if (!result.next())
			{
				throw new NotFoundException("Object with id '" + id +"' not found");
			}
			
			T instance = null;
			try {
				instance = serviceClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			resultParser.SetFields(instance, result);
			return instance;
			
		}finally{
			con.close();	
		}
	}
	
	/**
	 * This method retrieves all instances of IDbItem<T> from the database.
	 * @return
	 */
	@Override
	public List<T> GetAll() throws SQLException
	{
		Connection con = TransactionManager.createDatabaseConnection();
		
		try{
			String statement = String.format("SELECT * FROM %s.%s ;", dbName, tableName);
			PreparedStatement s = con.prepareStatement(statement);
			ResultSet result = s.executeQuery();
		
			List<T> resultList = getList(result);
			return resultList;
		}
		finally{
			con.close();	
		}
	}
	
	/**
	 * This method deletes an instances of IDbItem<T> from the database based on its id.
	 * @param id the id of the object to delete
	 * @param con the database connection for this transaction
	 * @return
	 */
	@Override
	public void Delete(Connection con, int id) throws SQLException, NotFoundException
	{
		String statement = String.format("DELETE FROM %s.%s WHERE id = ? ;", dbName, tableName);
		PreparedStatement s = con.prepareStatement(statement);
		s.setInt(1, id);
		s.execute();
	}
	
	/**
	 * This method updates an instances of IDbItem<T> on the database.
	 * @param con the database connection for this transaction
	 * @param object the object to update
	 * @return
	 */
	@Override
	public void Update(Connection con, T object) throws SQLException ,NotFoundException ,UnsupportedOperationException 
	{
		String statement = String.format("UPDATE %s.%s SET ", dbName, tableName);
		Map<String, Field> fields = resultParser.GetFields();
		List<Object> fieldValues = new ArrayList<>();
		Iterator<Entry<String, Field>> iterator = fields.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<String, Field> entry = iterator.next();
			try {
				Field field = entry.getValue();
				Object fieldValue = field.get(object);
				if(fieldValue != null  && !field.isAnnotationPresent(DbIgnore.class) && !field.isAnnotationPresent(Id.class))
				{
					statement += String.format("`%s` = ?", entry.getKey());
					statement += ", ";
					fieldValues.add(fieldValue);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		statement = statement.substring(0, statement.length()-2);
		
		statement += " WHERE id = ?;";
		fieldValues.add(object.getId());
		
		PreparedStatement s = con.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS);
		for(int i=0; i<fieldValues.size(); i++)
		{
			if(fieldValues.get(i).getClass().isEnum())
			{
				s.setObject(i+1, fieldValues.get(i).toString());
			}else{
				s.setObject(i+1, fieldValues.get(i));	
			}
		}
		
		s.execute();
	};
	
	/**
	 * This method inserts an instances of IDbItem<T> into the database.
	 * @param con the database connection for this transaction
	 * @param object the object to insert
	 * @return
	 */
	@Override
	public int Insert(Connection con, T object) throws SQLException ,NotFoundException ,UnsupportedOperationException 
	{
		String statement = String.format("INSERT INTO %s.%s (", dbName, tableName);
		Map<String, Field> fields = resultParser.GetFields();
		List<Object> fieldValues = new ArrayList<>();
		Iterator<Entry<String, Field>> iterator = fields.entrySet().iterator();
		
		while(iterator.hasNext())
		{
			Entry<String, Field> entry = iterator.next();
			try {
				Field field = entry.getValue();
				Object fieldValue = field.get(object);
				if(fieldValue != null  && !field.isAnnotationPresent(DbIgnore.class))
				{
					statement += "`" +entry.getKey()+ "`";
					statement += ", ";
					
					fieldValues.add(fieldValue);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		statement = statement.substring(0, statement.length()-2);
		statement += ") VALUES (";
		for(int i=0; i<fieldValues.size(); ++i)
		{
			statement += "?";
			if(i < fieldValues.size()-1)
				statement += ", ";
		}
		statement += ") ;";
		
		PreparedStatement s = con.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS);
		for(int i=0; i<fieldValues.size(); i++)
		{
			if(fieldValues.get(i).getClass().isEnum())
			{
				s.setObject(i+1, fieldValues.get(i).toString());
			}else{
				s.setObject(i+1, fieldValues.get(i));	
			}
		}
		
		s.execute();
	    ResultSet keys = s.getGeneratedKeys();
	    keys.next();
	    return keys.getInt(1);
	}
	
	/**
	 * This method retrieves a subset of instances of IDbItem<T> based on a number of predicates
	 * @param values the values that are used in the conditions
	 * @param conditions a list of conditions in the format "? = x", where ? stands for the value that is in the same position in the values list
	 * @return
	 */
	@Override
	public List<T> GetWhere(List<Object> values, String...conditions) throws SQLException
	{
		Connection con = TransactionManager.createDatabaseConnection();
		
		try{
			
			String statement = 
					  "SELECT * "
					+ "FROM %s.%s as t ";
			statement = String.format(statement, dbName, tableName);
			statement += "WHERE %s ";
			
			for(int i=1; i<conditions.length; i++)
			{
				statement += "AND %s ";
			}
			statement += ";";
			statement = String.format(statement, (Object[])conditions);
			
			PreparedStatement s = con.prepareStatement(statement);
			for(int i=0; i<values.size();i++)
			{
				s.setObject(i+1, values.get(i));
			}
			ResultSet result = s.executeQuery();
		
			List<T> resultList = getList(result);
			return resultList;
			
		}
		finally{
			con.close();	
		}
	}
	
	/**
	 * This method checks if an object with a specific exists in the database
	 * @param id the id to check
	 * @return
	 */
	@Override
	public boolean CheckId(int id) throws SQLException
	{
		Connection con = TransactionManager.createDatabaseConnection();
		
		try{
			String statement = String.format("SELECT * FROM %s.%s as t WHERE t.id = ? ;", dbName, tableName);
			PreparedStatement s = con.prepareStatement(statement);
			s.setInt(1, id);
			
			ResultSet result = s.executeQuery();
			
			if(!result.next())
				return false;
			
			return true;
			
		}finally{
			con.close();	
		}
	}
	
	/**
	 * This method retrieves a list of instances of IDbItem<T> from a ResultSet
	 * @param ResultSet the result object from a database query
	 * @return
	 */
	protected List<T> getList(ResultSet result) throws SQLException
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
