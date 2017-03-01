package ch.uzh.ifi.feedback.library.rest;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.inject.Injector;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;

/**
 * This class is the base class for all HttpServlets used in the feedback_repository and orchestrator projects.
 * It contains methods for the setup and invocation of the RestManager. It also starts the debug mode when the debug context parameter is set to true.
 * 
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public abstract class ServletBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    protected IRestManager _restManager;

	private IDatabaseConfiguration _dbConfig;;
    
    public ServletBase(IRestManager restManager, IDatabaseConfiguration config) {
		this._restManager = restManager;
		this._dbConfig = config;

        InitController();
	}
    
    /**
     * This method configures the RestManager with the package name containing the controller classes.
     */
    protected abstract void InitController();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Sets the headers for the response and invokes the corresponding method on the RestManager instance
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
		SetHeaders(response);
		_restManager.Get(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Sets the headers for the response and invokes the corresponding method on the RestManager instance
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
		SetHeaders(response);
		response.setStatus(201);
		_restManager.Post(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Sets the headers for the response and invokes the corresponding method on the RestManager instance
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
		SetHeaders(response);
		_restManager.Delete(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 * Sets the headers for the response and invokes the corresponding method on the RestManager instance
	 */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		SetDebugMode();
        SetHeaders(response);
		_restManager.Put(request, response);
	}
	
	/**
	 * @see HttpServlet#doOptions(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SetHeaders(response);
		response.setStatus(200);
	}
	
	
	private void SetHeaders(HttpServletResponse response)
	{
        response.setContentType("application/json; charset=utf-8");            
        //response.setCharacterEncoding("UTF-8");
        
        response.setHeader("Access-Control-Allow-Origin", "http://dev.energiesparkonto.de");
        response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Connection", "keep-alive");
		response.setHeader("X-Frame-Options", "SAMEORIGIN");
		//response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Access-Control-Allow-Headers");
        response.setHeader("Access-Control-Max-Age", "86400");
	}
	
	/**
	 * Sets the Debug mode of the DatabaseConfiguration to "true" if the ServletContext parameter "debug" is set to "true".
	 * If "debug" is set to "true", all queries will be executed against the test database configured in the DatabaseConfiguration.
	 */
	private void SetDebugMode()
	{
    	String debugMode = getServletContext().getInitParameter("debug");
    	if(debugMode != null && debugMode.equalsIgnoreCase("true"))
    	{
    		_dbConfig.StartDebugMode();
    	}
	}
}
