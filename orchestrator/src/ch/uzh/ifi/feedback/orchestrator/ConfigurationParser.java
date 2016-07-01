package ch.uzh.ifi.feedback.orchestrator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import ch.uzh.ifi.feedback.library.transaction.*;

public class ConfigurationParser implements IDbResultParser<List<FeedbackMechanism>> {

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
				mechanism.setActive((resultSet.getInt("active") != 0));
				mechanism.setOrder(resultSet.getInt("order"));
				mechanism.setCanBeActivated(resultSet.getInt("can_be_activated") != 0);
				config.getFeedbackMechanisms().add(mechanism);
			}
			
			FeedbackParameter param = new FeedbackParameter();
			param.setKey(resultSet.getString("key"));
			param.setValue(getValue(resultSet, "value"));
			param.setDefaultValue(getValue(resultSet, "default_value"));
			param.setEditableByUser(resultSet.getString("editable_by_user") == null ? null : (resultSet.getInt("editable_by_user") != 0));
			currentParameters.add(param);
	    }
	    
	    mechanism.getParameters().addAll(currentParameters);
	    
	    result = config;
	}
	
	private Object getValue(ResultSet resultSet, String columnName) throws SQLException
	{
		String stringValue = resultSet.getString(columnName);
		
		if(stringValue == null)
			return null;
		
		if(tryParseInt(stringValue))
			return Integer.parseInt(stringValue);
		
		if(tryParseDouble(stringValue))
			return Double.parseDouble(stringValue);
		
		if (stringValue.equalsIgnoreCase("true") || stringValue.equalsIgnoreCase("false")) {
		    return Boolean.valueOf(stringValue);
		}
		
		return stringValue;
	}
	
	private boolean tryParseInt(String value) {  
	     try {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      }  
	}
	
	private boolean tryParseDouble(String value) {  
	     try {  
	         Double.parseDouble(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      }  
	}

	@Override
	public List<FeedbackMechanism> GetResult() {
		return result.getFeedbackMechanisms();
	}
}
