package ch.uzh.ifi.feedback.library.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRestController<T> {
	
	T Get(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	default void Put(HttpServletRequest request, HttpServletResponse response, T obj) throws Exception
    {
    	response.setStatus(503);
    }
	
	default void Delete(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setStatus(503);
	}
	
	default void Post(HttpServletRequest request, HttpServletResponse response, T obj) throws Exception
	{
		response.setStatus(503);
	}
	
	String Serialize(T object);
	
	T Deserialize(String content);
	
}
