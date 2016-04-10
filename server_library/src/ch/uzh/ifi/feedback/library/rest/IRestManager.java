package ch.uzh.ifi.feedback.library.rest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRestManager
{
	void Init(String packageName) throws Exception;
	
	void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException;
	
	default void Put(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		Get(request, response);
	}
	
	default void Delete(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		Get(request, response);
	}
	
	default void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		Get(request, response);
	}
}
