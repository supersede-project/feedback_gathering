import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SchemaServlet extends HttpServlet {
    
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String textTest = "{ key1: 'value1', key2: 'value2' }";

    	response.setContentType("application/json");  	// Set content type of the response so that jQuery knows what it can expect.
    	response.setCharacterEncoding("UTF-8"); 		// You want world domination, huh?
    	response.getWriter(textTest);       			// Write response body.
	}
}