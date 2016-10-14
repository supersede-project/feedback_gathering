package ch.uzh.ifi.feedback.library.rest.routing;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.authorization.UserRole;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import ch.uzh.ifi.feedback.library.rest.service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;

public class HandlerInfo {
	private Method method;
	private HttpMethod httpMethod;
	private Class<?> handlerClazz;
	private Map<String, Parameter> pathParameters;
	private UriTemplate uriTemplate;
	private Class<?> serializedParameterClass;
	private Authenticate auth;

	
	public HandlerInfo(
			Method method, 
			HttpMethod httpMethod, 
			Class<?> handlerClazz,
			Class<?> parameterClass,
			UriTemplate template,
			Authenticate auth)
	{
		this.method = method;
		this.httpMethod = httpMethod;
		this.handlerClazz = handlerClazz;
		this.uriTemplate = template;
		this.serializedParameterClass = parameterClass;
		this.setAuth(auth);
		pathParameters = new LinkedHashMap<>();
	}

	public Class<?> getHandlerClass(){
		return this.handlerClazz;
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

	public Authenticate getAuth() {
		return auth;
	}

	public void setAuth(Authenticate auth) {
		this.auth = auth;
	}
}
