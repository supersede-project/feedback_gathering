package ch.uzh.ifi.feedback.library.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public abstract class RestController<T> implements IRestController<T> {

	private Class<T> parameterType;
	
	public RestController()
	{
		setParameterType();
	}
	
	private void setParameterType()
	{
		Type superclass = this.getClass().getGenericSuperclass();
		
		Class<T> parametertype;
		while(superclass instanceof ParameterizedType)
		{
			superclass = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		}
		
		parameterType = (Class<T>)(superclass);
	}
	
	@Override
	public abstract T Get(HttpServletRequest request, HttpServletResponse response) throws Exception;

	@Override
	public String Serialize(T object) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(object);
		
		return json;
	}

	@Override
	public T Deserialize(String content) {
		
		Gson gson = new Gson();
		T requestObject = gson.fromJson(content, parameterType);
		
		return requestObject;
	}

}
