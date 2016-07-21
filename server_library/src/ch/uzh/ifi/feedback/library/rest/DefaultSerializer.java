package ch.uzh.ifi.feedback.library.rest;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.ISerializationService;

public class DefaultSerializer<T> implements ISerializationService<T> {

	private Type serializationType;
	
	public DefaultSerializer(Type serializationType) {
		this.serializationType = serializationType;
	}
	
	@Override
	public String Serialize(T object) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		String json = gson.toJson(object);
		
		return json;
	}

	@Override
	public T Deserialize(String data) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		T requestObject = gson.fromJson(data, serializationType);

		return requestObject;
	}

}
