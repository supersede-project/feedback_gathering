package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRestController<T> {
	
	default T Get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
			return null;
			
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	default void Put(HttpServletRequest request, HttpServletResponse response, T object) throws Exception
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	default void Delete(HttpServletRequest request, HttpServletResponse response, T object) throws Exception
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	default void Post(HttpServletRequest request, HttpServletResponse response, T object) throws Exception
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	String Serialize(T object);
	
	T Deserialize(String content);
	
}
