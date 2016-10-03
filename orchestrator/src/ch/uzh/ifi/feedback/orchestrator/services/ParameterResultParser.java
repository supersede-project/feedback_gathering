package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

@Singleton
public class ParameterResultParser extends DbResultParser<FeedbackParameter> {

	public ParameterResultParser() {
		super(FeedbackParameter.class);
	}

	@Override
	public void SetFields(FeedbackParameter param, ResultSet rs) throws SQLException {
		super.SetFields(param, rs);
		
		Object value = param.getValue();
		if(!(value instanceof List) && value != null)
		{
			param.setValue(parseValue(value.toString()));
		}
	}
	
	private Object parseValue(String value)
	{
		if(tryParseInt(value))
			return Integer.parseInt(value);
		
		if(tryParseDouble(value))
			return Double.parseDouble(value);
		
		return value;
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
	
}
