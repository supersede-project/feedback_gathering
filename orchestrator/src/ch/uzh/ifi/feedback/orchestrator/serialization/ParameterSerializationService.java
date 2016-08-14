package ch.uzh.ifi.feedback.orchestrator.serialization;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.internal.LinkedTreeMap;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackParameter;

public class ParameterSerializationService extends OrchestratorSerializationService<FeedbackParameter>{
	
	@Override
	public FeedbackParameter Deserialize(HttpServletRequest request) {
		FeedbackParameter param =  super.Deserialize(request);
		SetNestedParameters(param);
		return param;
	}
	
	@Override
	public void SetNestedParameters(FeedbackParameter param)
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
			parsedChildren.stream().forEach(c -> SetNestedParameters(c));
			param.setValue(parsedChildren);
		}
	}
}
