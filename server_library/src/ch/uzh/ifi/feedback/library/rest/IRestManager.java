package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRestManager
{

	void Init(String packageName) throws Exception;
	
	default void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	default void Put(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	default void Delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
	
	default void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try{
			response.setStatus(405);
			response.getWriter().append("Operation not supported for this resource!");
		}catch(IOException ex){
			throw new ServletException(ex);
		}
	}
}
