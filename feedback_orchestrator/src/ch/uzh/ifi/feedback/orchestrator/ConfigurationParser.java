package ch.uzh.ifi.feedback.orchestrator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import ch.uzh.ifi.feedback.library.transaction.*;

public class ConfigurationParser implements IDbResultParser<Configuration> {

	private Configuration result;
	
	@Override
	public void Parse(ResultSet resultSet) throws SQLException {
		
		Configuration config = new Configuration();
		String currentMechanism = null;
		List<FeedbackParameter> currentParameters = new ArrayList<>();
		FeedbackMechanism mechanism = null;
	    
	    while(resultSet.next()){
	    	
	    	if(currentMechanism == null)
				mechanism = new FeedbackMechanism();
	    	
	    	String mechanismName = resultSet.getString("mechanism_name");
	    	
	    	if(!mechanismName.equals(currentMechanism) && currentMechanism != null)
	    	{
	    		mechanism.getParameters().addAll(currentParameters);
	    		currentParameters.clear();
				mechanism = new FeedbackMechanism();
	    	}
	    	
    		currentMechanism = mechanismName;

			if(!config.getFeedbackMechanisms().contains(mechanism))
			{
				mechanism.setType(currentMechanism);
				mechanism.setActive(Boolean.parseBoolean(resultSet.getString("active")));
				mechanism.setOrder(Integer.parseInt(resultSet.getString("order")));
				mechanism.setCanBeActivated(Boolean.parseBoolean(resultSet.getString("can_be_activated")));
				config.getFeedbackMechanisms().add(mechanism);
			}
			
			FeedbackParameter param = new FeedbackParameter();
			param.setKey(resultSet.getString("key"));
			param.setValue(resultSet.getString("value"));
			param.setDefaultValue(resultSet.getString("default_value"));
			param.setEditableByUser(resultSet.getString("editable_by_user") == null ? null : Boolean.parseBoolean(resultSet.getString("editable_by_user")));
			currentParameters.add(param);
	    }
	    
	    mechanism.getParameters().addAll(currentParameters);
	    
	    result = config;
	}

	@Override
	public Configuration GetResult() {
		// TODO Auto-generated method stub
		return result;
	}
}
