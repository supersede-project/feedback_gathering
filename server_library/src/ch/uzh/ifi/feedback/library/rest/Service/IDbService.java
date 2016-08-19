package ch.uzh.ifi.feedback.library.rest.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.OperationNotSupportedException;
import javax.xml.bind.helpers.NotIdentifiableEventImpl;

import javassist.NotFoundException;

public interface IDbService<T> {
	
	String GetLanguage();
	
	void SetLanguage(String lang);
	
	T GetById(int id) throws SQLException, NotFoundException;
	
	boolean CheckId(int id) throws SQLException;
	
	List<T> GetAll() throws SQLException, NotFoundException;
	
	List<T> GetWhere(List<Object> values, String...conditions) throws SQLException, NotFoundException;
	
	void Delete(Connection con, int id) throws SQLException, NotFoundException;
	
	default void Update(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	default int Insert(Connection con, T object) throws SQLException, NotFoundException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
