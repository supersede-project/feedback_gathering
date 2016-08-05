package ch.uzh.ifi.feedback.library.rest.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javassist.NotFoundException;

public interface IDbService<T> {
	
	T GetById(Connection con, int id) throws SQLException, NotFoundException;
	
	List<T> GetAll(Connection con) throws SQLException, NotFoundException;
	
	List<T> GetAllFor(Connection con, String foreignKeyName, int foreignKey) throws SQLException, NotFoundException;
	
	default void Update(Connection con, T object) throws SQLException, NotFoundException {
	}
	
	void UpdateFor(Connection con, T object, String foreignKeyName, int foreignKey) throws SQLException, NotFoundException;
	
	default void Insert(Connection con, T object) throws SQLException, NotFoundException {
	}
	
	void InsertFor(Connection con, T object, String foreignKeyName, int foreignKey) throws SQLException, NotFoundException;
}
