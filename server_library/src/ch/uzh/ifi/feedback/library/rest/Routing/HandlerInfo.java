package ch.uzh.ifi.feedback.library.rest.Routing;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;

public class HandlerInfo {
	private Method method;
	private HttpMethod httpMethod;
	private RestController<?> handlerInstance;
	private Map<String, Parameter> pathParameters;
	private UriTemplate uriTemplate;
	private Class<?> serializedParameterClass;

	public HandlerInfo(
			Method method, 
			HttpMethod httpMethod, 
			RestController<?> handlerInstance, 
			UriTemplate template)
	{
		this.method = method;
		this.httpMethod = httpMethod;
		this.handlerInstance = handlerInstance;
		this.uriTemplate = template;
		pathParameters = new HashMap<>();
	}

	public RestController<?> getHandlerInstance(){
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

	public UriTemplate getUriTemplate() {
		return uriTemplate;
	}

	public void setUriTemplate(UriTemplate uriTemplate) {
		this.uriTemplate = uriTemplate;
	}

	public Class<?> getSerializedParameterClass() {
		return serializedParameterClass;
	}

	public void setSerializedParameterClass(Class<?> serializedParameterClass) {
		this.serializedParameterClass = serializedParameterClass;
	}
}
