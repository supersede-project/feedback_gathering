package ch.uzh.ifi.feedback.library.rest.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.helpers.NotIdentifiableEventImpl;

import javassist.NotFoundException;

public interface IDbService<T> {
	
	T GetById(int id) throws SQLException, NotFoundException;
	
	default boolean CheckId(int id) throws SQLException{
		try{
			GetById(id);
			return true;
		}catch(NotFoundException e)
		{
			return false;
		}
	}
	
	List<T> GetAll() throws SQLException;
	
	List<T> GetWhere(List<Object> values, String...conditions) throws SQLException;
	
	default void Delete(Connection con, int id) throws SQLException, NotFoundException {
		throw new UnsupportedOperationException();
	}
	
	default void Update(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	default int Insert(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
