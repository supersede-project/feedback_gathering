package ch.uzh.ifi.feedback.orchestrator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ch.uzh.ifi.feedback.library.transaction.IDbResultParser;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

public class ConfigurationService {

	private TransactionManager transactionManager;
	private IDbResultParser<List<FeedbackMechanism>> dbResultParser;
	
	public ConfigurationService(TransactionManager transactionManager, IDbResultParser<List<FeedbackMechanism>> dbResultParser)
	{
		this.transactionManager = transactionManager;
		this.dbResultParser = dbResultParser;
	}

	public List<FeedbackMechanism> Read(String application) throws Exception {
		transactionManager.withTransaction((con) -> {
		    dbResultParser.Parse(ExecuteTransaction(con, application));
		});
		
		return dbResultParser.GetResult();
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
}
