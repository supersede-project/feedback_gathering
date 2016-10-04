package ch.uzh.ifi.feedback.repository;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.IRestController;
import ch.uzh.ifi.feedback.library.rest.IRestManager;
import ch.uzh.ifi.feedback.library.rest.RestManager;
import ch.uzh.ifi.feedback.library.rest.ServletBase;
import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;

/**
 * Servlet implementation class FeedbackServlet
 */
@Singleton
public class FeedbackServlet extends ServletBase {
	
	private static final long serialVersionUID = 1L;    
    
    /**
     * @see HttpServlet#HttpServlet()
     */
	@Inject
    public FeedbackServlet(IRestManager restManager, DatabaseConfiguration config) {
        super(restManager, config);
    }
    
    @Override
    protected void InitController()
    {
        try{
        	_restManager.Init("ch.uzh.ifi.feedback.repository");
        }
        catch(Exception ex){
        	System.out.println(ex.getMessage());
        	ex.printStackTrace();
        	super.destroy();
        }
    }
}
