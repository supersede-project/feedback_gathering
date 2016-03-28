package ch.uzh.ifi.feedback.orchestrator;

import java.sql.ResultSet;
import java.sql.SQLException;

import ch.uzh.ifi.feedback.library.transaction.*;

public class ConfigurationParser implements IDbResultParser<Configuration> {

	private Configuration result;
	
	@Override
	public void Parse(ResultSet resultSet) throws SQLException {
		
		Configuration config = new Configuration();
	    
	    while(resultSet.next()){
	    	   
	    	String mechanismName = resultSet.getString("mechanism_name");

			if(config.getFeedbackMechanisms().get(mechanismName) == null)
			{
				FeedbackMechanism mechanism = new FeedbackMechanism();
				config.getFeedbackMechanisms().put(mechanismName, mechanism);
				mechanism.setActive(Integer.parseInt(resultSet.getString("active")) != 0);
			}
			
			config.getFeedbackMechanisms().get(mechanismName).getParameters().put(resultSet.getString("key"), resultSet.getString("value"));
		
	    }
	    
	    result = config;
	}

	@Override
	public Configuration GetResult() {
		// TODO Auto-generated method stub
		return result;
	}
}
