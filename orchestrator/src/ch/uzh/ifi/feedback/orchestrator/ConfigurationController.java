package ch.uzh.ifi.feedback.orchestrator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.*;
import ch.uzh.ifi.feedback.library.transaction.*;


@Controller
(Route = "/{Application}/configuration")
public class ConfigurationController extends RestController<List<FeedbackMechanism>>{
    
    public ConfigurationController() {
    }

    @Override
	public List<FeedbackMechanism> Get(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String application = (String)request.getAttribute("Application");
		
    	IDbResultParser<List<FeedbackMechanism>> parser = new ConfigurationParser();
    	
		TransactionManager.withTransaction((con) -> {
			
		    PreparedStatement s = con.prepareStatement(

		    		"Select m.name as mechanism_name, m.order, m.active, m.can_be_activated, p.key, p.value, p.default_value, p.editable_by_user FROM "
		    		+ "feedback_orchestrator.mechanisms as m "
		    		+ "LEFT JOIN feedback_orchestrator.applications as a on (m.application_id = a.id) "
		    		+ "LEFT JOIN feedback_orchestrator.parameters as p on (p.mechanism_id = m.id) "
		    		+ "WHERE a.name = ? ;"

		    		
		    		);
		    
		    s.setString(1, application);
		    ResultSet result = s.executeQuery();
		    
		    parser.Parse(result);
		    	    	
		});

		return parser.GetResult();
	}
}
