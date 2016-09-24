package ch.uzh.ifi.feedback.orchestrator;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;
import ch.uzh.ifi.feedback.library.rest.ServletBase;
import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;

/**
 * Servlet implementation class OrchestratorServlet
 */
@WebServlet("/")
public class OrchestratorServlet extends ServletBase {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrchestratorServlet() {
        super();
        
        this.InitController();
    }
    
    @Override
    protected void InitController()
    {
        this._restController = new RestManager();
        
        try{
        	_restController.Init("ch.uzh.ifi.feedback.orchestrator");
        }
        catch(Exception ex){
        	System.out.println(ex.getMessage());
        	ex.printStackTrace();
        	super.destroy();
        }
    }
}
