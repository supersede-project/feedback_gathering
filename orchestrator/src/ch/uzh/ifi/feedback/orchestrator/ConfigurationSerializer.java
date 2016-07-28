package ch.uzh.ifi.feedback.orchestrator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import ch.uzh.ifi.feedback.library.rest.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.Model.Application;
import ch.uzh.ifi.feedback.orchestrator.Model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.Model.FeedbackParameter;
import ch.uzh.ifi.feedback.orchestrator.Model.GeneralConfiguration;
import ch.uzh.ifi.feedback.orchestrator.Model.PullConfiguration;

public class ConfigurationSerializer extends DefaultSerializer<Application> {

	public ConfigurationSerializer(Type serializationType) {
		super(serializationType);
	}
	
	@Override
	public String Serialize(Application object) {
		return super.Serialize(object);
	}

	@Override
	public Application Deserialize(String data) {
		Application app = super.Deserialize(data);
		
		for(FeedbackMechanism mechanism : app.getFeedbackMechanisms())
		{
			setNestedParameters(mechanism.getParameters());
		}
		
		for(GeneralConfiguration generalConfig : app.getGeneralConfigurations())
		{
			setNestedParameters(generalConfig.getParameters());
		}
		
		for(PullConfiguration pullConfig : app.getPullConfigurations())
		{
			setNestedParameters(pullConfig.getParameters());
		}
		
		return app;
	}
	
	private void setNestedParameters(List<FeedbackParameter> params)
	{
		for(FeedbackParameter param : params)
		{
			if(List.class.isAssignableFrom(param.getValue().getClass())){
				List<LinkedTreeMap<String, Object>> children = (List<LinkedTreeMap<String, Object>>)param.getValue();
				List<FeedbackParameter> parsedChildren = new ArrayList<>();
				for(LinkedTreeMap<String, Object> map : children){
					FeedbackParameter child = new FeedbackParameter();
					child.setKey((String)map.get("key"));
					child.setValue(map.get("value"));
					
					if(map.containsKey("editable_by_user"))
						child.setEditableByUser((Boolean)map.get("editable_by_user"));
					
					if(map.containsKey("default_value"))
						child.setDefaultValue(map.get("default_value"));
					
					if(map.containsKey("language"))
						child.setDefaultValue(map.get("language"));
					
					parsedChildren.add(child);
				}
				setNestedParameters(parsedChildren);
				param.setValue(parsedChildren);
			}
		}
	}

}
