package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

public interface IRestController<T> {
	
	default T GetById(int id) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	default List<T> GetAll() throws Exception {
		throw new UnsupportedOperationException();
	}
	
}
