package ch.uzh.ifi.feedback.library.rest.routing;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ch.uzh.ifi.feedback.library.rest.RestController;
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
	private Class<? extends ITokenAuthenticationService> authenticationClass;
	private UserRole authenticationRole;
	
	public HandlerInfo(
			Method method, 
			HttpMethod httpMethod, 
			Class<?> handlerClazz,
			Class<?> parameterClass,
			UriTemplate template,
			Class<? extends ITokenAuthenticationService> authClass,
			UserRole authRole)
	{
		this.method = method;
		this.httpMethod = httpMethod;
		this.handlerClazz = handlerClazz;
		this.uriTemplate = template;
		this.serializedParameterClass = parameterClass;
		this.authenticationClass = authClass;
		this.authenticationRole = authRole;
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

	public Class<? extends ITokenAuthenticationService> getAuthenticationClass() {
		return authenticationClass;
	}

	public void setAuthenticationClass(Class<? extends ITokenAuthenticationService> authenticationClass) {
		this.authenticationClass = authenticationClass;
	}

	public UserRole getAuthenticationUserRole() {
		return authenticationRole;
	}

	public void setAuthenticationUserRole(UserRole authenticationUserRole) {
		this.authenticationRole = authenticationUserRole;
	}
}
