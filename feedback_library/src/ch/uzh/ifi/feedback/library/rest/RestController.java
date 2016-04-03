package ch.uzh.ifi.feedback.library.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
<<<<<<< HEAD
import com.google.gson.reflect.TypeToken;
=======
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public abstract class RestController<T> implements IRestController<T> {

<<<<<<< HEAD
	private Class<?> parameterType;
	private Type serializationType;
=======
	private Class<T> parameterType;
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
	
	public RestController()
	{
		setParameterType();
	}
	
	private void setParameterType()
	{
		Type superclass = this.getClass().getGenericSuperclass();
<<<<<<< HEAD
		serializationType = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		
=======
		
		Class<T> parametertype;
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
		while(superclass instanceof ParameterizedType)
		{
			superclass = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		}
		
<<<<<<< HEAD
		parameterType = (Class<?>)(superclass);
=======
		parameterType = (Class<T>)(superclass);
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
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
<<<<<<< HEAD
		T requestObject = gson.fromJson(content, serializationType);
=======
		T requestObject = gson.fromJson(content, parameterType);
>>>>>>> 6f22e25d64089156b90885a64297afa8ea0a6907
		
		return requestObject;
	}

}
