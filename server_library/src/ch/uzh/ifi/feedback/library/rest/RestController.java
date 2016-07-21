package ch.uzh.ifi.feedback.library.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

public abstract class RestController<T> implements IRestController<T> {

	private Class<?> parameterType;
	private Type serializationType;
	
	protected Type getSerializationType() {
		return serializationType;
	}

	public RestController()
	{
		setParameterType();
	}
	
	private void setParameterType()
	{
		Type superclass = this.getClass().getGenericSuperclass();
		serializationType = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		
		while(superclass instanceof ParameterizedType)
		{
			superclass = ((ParameterizedType)superclass).getActualTypeArguments()[0];
		}
		
		parameterType = (Class<?>)(superclass);
	}

}
