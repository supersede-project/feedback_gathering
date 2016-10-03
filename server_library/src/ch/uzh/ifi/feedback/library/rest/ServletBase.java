package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;

/**
 * Servlet base implementation
 */
public abstract class ServletBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    protected IRestManager _restManager;
    protected DatabaseConfiguration _dbConfig;
    
    public ServletBase(IRestManager restManager, DatabaseConfiguration config) {
		this._restManager = restManager;
		this._dbConfig = config;
		
        InitController();
	}
    
    protected abstract void InitController();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
		SetHeaders(response);
		_restManager.Get(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
		SetHeaders(response);
		_restManager.Post(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
		SetHeaders(response);
		_restManager.Delete(request, response);
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
        SetHeaders(response);
		_restManager.Put(request, response);
	}
	
	private void SetHeaders(HttpServletResponse response)
	{
        response.setContentType("application/json");            
        response.setCharacterEncoding("UTF-8");
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
	}
	
	private void SetDebugMode()
	{
    	String debugMode = getServletContext().getInitParameter("debug");
    	if(debugMode != null && debugMode.equalsIgnoreCase("true"))
    	{
    		_dbConfig.StartDebugMode();
    	}
	}
}
