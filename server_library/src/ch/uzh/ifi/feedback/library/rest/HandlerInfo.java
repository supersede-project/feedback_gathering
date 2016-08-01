package ch.uzh.ifi.feedback.library.rest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class HandlerInfo {
	private Method method;
	private HttpMethod httpMethod;
	private Object handlerInstance;
	private Map<String, Parameter> pathParameters;
	private UriTemplate uriTemplate;
	private Class<? extends ISerializationService> serializationClass;

	public HandlerInfo(Method method, HttpMethod httpMethod, Object handlerInstance, UriTemplate template)
	{
		this.method = method;
		this.httpMethod = httpMethod;
		this.handlerInstance = handlerInstance;
		this.uriTemplate = template;
		pathParameters = new HashMap<>();
	}

	public Object getHandlerInstance(){
		return this.handlerInstance;
	}
	
	public HttpMethod GetHttpMethod(){
		return this.httpMethod;
	}
	
	public Method getMethod(){
		return this.method;
	}
	
	public Map<String, Parameter> getPathParameters() {
		return pathParameters;
	}

	public Class<? extends ISerializationService> getSerializationClass() {
		return serializationClass;
	}

	public void setSerializationClass(Class<? extends ISerializationService> serializationClass) {
		this.serializationClass = serializationClass;
	}

	public UriTemplate getUriTemplate() {
		return uriTemplate;
	}

	public void setUriTemplate(UriTemplate uriTemplate) {
		this.uriTemplate = uriTemplate;
	}
}
