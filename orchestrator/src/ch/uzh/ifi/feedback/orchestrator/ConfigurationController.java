package ch.uzh.ifi.feedback.orchestrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.uzh.ifi.feedback.library.rest.*;
import ch.uzh.ifi.feedback.library.transaction.*;
import ch.uzh.ifi.feedback.orchestrator.interfaces.ConfigurationProvider;


@Controller
(Route = "/{Application}/configuration")
public class ConfigurationController extends RestController<List<FeedbackMechanism>> implements ConfigurationProvider{
    
    public ConfigurationController(TransactionManager transactionManager) {
		super(transactionManager);
	}

	@Override
	public List<FeedbackMechanism> Get(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String application = (String)request.getAttribute("Application");
		return GetFeedbackMechanisms(application);
	}
	
	public ResultSet ExecuteTransaction(Connection con, String application) throws SQLException
	{
	    PreparedStatement s = con.prepareStatement(

	    		"Select m.name as mechanism_name, m.order, m.active, m.can_be_activated, p.key, p.value, p.default_value, p.editable_by_user FROM "
	    		+ "feedback_orchestrator.mechanisms as m "
	    		+ "LEFT JOIN feedback_orchestrator.applications as a on (m.application_id = a.id) "
	    		+ "LEFT JOIN feedback_orchestrator.parameters as p on (p.mechanism_id = m.id) "
	    		+ "WHERE a.name = ? ;"		    		
	    		);
	    
	    s.setString(1, application);
	    ResultSet result = s.executeQuery();
	    
	    return result;
	}

	@Override
	public List<FeedbackMechanism> GetFeedbackMechanisms(String application) throws Exception {
    	IDbResultParser<List<FeedbackMechanism>> parser = new ConfigurationParser();
    	
		getTransactionManager().withTransaction((con) -> {
		    parser.Parse(ExecuteTransaction(con, application));
		});
		
		return parser.GetResult();
	}
}
