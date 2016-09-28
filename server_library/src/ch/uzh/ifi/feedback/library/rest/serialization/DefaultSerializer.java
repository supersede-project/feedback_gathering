package ch.uzh.ifi.feedback.library.rest.serialization;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class DefaultSerializer<T> implements ISerializationService<T> {

	protected Type serializationType;
	
	public DefaultSerializer() {
		setSerializationType();
	}
	
	@Override
	public String Serialize(T object) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		String json = gson.toJson(object);
		
		return json;
	}

	@Override
	public T Deserialize(HttpServletRequest request) 
	{
		String data = GetRequestContent(request);
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		T requestObject = gson.fromJson(data, serializationType);

		return requestObject;
	}
	
	private void setSerializationType()
	{
		Type superclass = this.getClass().getGenericSuperclass();
		serializationType = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		
		while(superclass instanceof ParameterizedType)
		{
			superclass = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		}
	}

	private String GetRequestContent(HttpServletRequest request)
	{
	    StringBuffer buffer = new StringBuffer();
		String line = null;
		
		try{
			BufferedReader reader = request.getReader();
			while((line = reader.readLine()) != null)
			{
				buffer.append(line);
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		return buffer.toString();
	}
}
