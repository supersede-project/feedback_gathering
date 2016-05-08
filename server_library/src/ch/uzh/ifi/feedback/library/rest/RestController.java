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
	private TransactionManager transactionManager;
	
	public Type getSerializationType() {
		return serializationType;
	}

	public void setSerializationType(Type serializationType) {
		this.serializationType = serializationType;
	}

	public RestController(TransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
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

	@Override
	public String Serialize(T object) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		String json = gson.toJson(object);
		
		return json;
	}

	@Override
	public T Deserialize(String content) throws JsonSyntaxException {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		T requestObject = gson.fromJson(content, serializationType);

		return requestObject;
	}

	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

}
