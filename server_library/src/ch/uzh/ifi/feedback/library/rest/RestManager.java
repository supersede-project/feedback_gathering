package ch.uzh.ifi.feedback.library.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.rmi.AlreadyBoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.el.MethodNotFoundException;
import javax.naming.AuthenticationException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.reflections.*;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.gson.JsonSyntaxException;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.annotations.Authenticate;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.annotations.Validate;
import ch.uzh.ifi.feedback.library.rest.authorization.AuthorizationException;
import ch.uzh.ifi.feedback.library.rest.authorization.ITokenAuthenticationService;
import ch.uzh.ifi.feedback.library.rest.authorization.UserRole;
import ch.uzh.ifi.feedback.library.rest.routing.HandlerInfo;
import ch.uzh.ifi.feedback.library.rest.routing.HttpMethod;
import ch.uzh.ifi.feedback.library.rest.routing.UriTemplate;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import ch.uzh.ifi.feedback.library.rest.service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import javassist.NotFoundException;


/**
 * This class is responsible for managing and invoking controller methods with the correct parameters.
 * Each controller method is annotated with an @Path-attribute that represents a URL template.
 * When a request arrives at a Servlet method, the corresponding method (e.g. Get) on an instance of this class is called and the handler method
 * with the associated @Path attribute is invoked.
 *
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public class RestManager implements IRestManager {

	private List<HandlerInfo> _handlers;
	private Map<Class<?>, Method> _parserMap;
	private Map<Class<?>, Class<? extends ISerializationService<?>>> _serializerMap;
	private Injector _injector;

	@Inject
	public RestManager(Injector injector) {
		
		this._injector = injector;
		
		_handlers = new ArrayList<>();
		_parserMap = new HashMap<>();
		_serializerMap = new HashMap<>();
	}


	/**
	  * This method is used to start the initialization of the controller handler list from the provided package name.
	  * @param packageName the fully qualified package name where the controller classes reside
	 */
	public void Init(String packageName) throws Exception
	{
		
		InitParserMap();
		
		Collection<URL> packages = ClasspathHelper.forPackage(packageName);
		packages.addAll(ClasspathHelper.forPackage("ch.uzh.ifi.feedback.library.rest"));
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(packages)
                .setScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner()));
		
		InitHandlerTable(reflections);
	}
	
	/**
	  * This method is used to initialize the controller handler list from the properly initialized reflections object.
	  * It iterates over all controller annotated classes and stores the associated serializer class in the _serializerMap.
	  * It then iterates over all methods of the class and stores the following properties in the _handlers list:
	  * the url template, the http method, the authentication class and the path parameters.
	  *
	  * @param reflections Reflections object that is initialized with the package containing the controller classes.
	 */
	private void InitHandlerTable(Reflections reflections) throws Exception
	{
		Set<Class<?>> controllerAnnotated = reflections.getTypesAnnotatedWith(Controller.class);
		for(Class<?> clazz : controllerAnnotated){
			Class<?> parameterClass = clazz.getAnnotation(Controller.class).value();
			
			if(parameterClass.isAnnotationPresent(Serialize.class))
			{
				Class<? extends ISerializationService<?>> serializerClass = parameterClass.getAnnotation(Serialize.class).value();
				_serializerMap.put(parameterClass, serializerClass);
			}
			
			for(Method m : clazz.getMethods()){
				if(m.isAnnotationPresent(Path.class)){
					
					String annotatedPath = m.getAnnotation(Path.class).value();	
					String path = annotatedPath;
					UriTemplate template = UriTemplate.Parse(path);
					
					HttpMethod method = HttpMethod.GET;
					if(m.isAnnotationPresent(POST.class)){
						method = HttpMethod.POST;
					}else if(m.isAnnotationPresent(PUT.class))
					{
						method = HttpMethod.PUT;
					}else if(m.isAnnotationPresent(DELETE.class))
					{
						method = HttpMethod.DELETE;
					}
					
					final HttpMethod methodFinal = method;
					
					if(_handlers.stream().anyMatch((h) -> h.GetHttpMethod() == methodFinal && h.getUriTemplate().Match(path) != null))
					{
						continue;
					}

					Authenticate auth = null;
					if(m.isAnnotationPresent(Authenticate.class))
					{
						auth = m.getAnnotation(Authenticate.class);
					}
					
					HandlerInfo info = new HandlerInfo(m, method, clazz, parameterClass, template, auth);
					
					Parameter[] params = m.getParameters();
					for(int i = 0; i < params.length; i++){
						if (params[i].isAnnotationPresent(PathParam.class)){
							
							if(!_parserMap.containsKey(params[i].getType()) && !params[i].getType().equals(String.class))
								throw new Exception("The provided parameter type is not supported!");
							
							String paramName = params[i].getAnnotation(PathParam.class).value();
							info.getPathParameters().put(paramName, params[i]);
						}else if(i < params.length - 1){
							throw new Exception("No annotation for parameter " + params[i].getName() + " present!/n");
						}
					}

					_handlers.add(info);
				}
			}
		}
	}

	/**
	  * This method is used the parser map that is used for parsing the path parameters annotated with the @PathParam attribute
	 */
	private void InitParserMap() throws Exception {
		
			_parserMap.put(Integer.class, Integer.class.getMethod("parseInt", String.class));
			_parserMap.put(Double.class, Double.class.getMethod("parseDouble", String.class));
			_parserMap.put(Long.class, Long.class.getMethod("parseLong", String.class));
			_parserMap.put(Float.class, Float.class.getMethod("parseFloat", String.class));
			_parserMap.put(Timestamp.class, Timestamp.class.getMethod("valueOf", String.class));
	}
	
	/**
	  * This method is called from the "doGet" method of a HttpServlet.
	  * It calls the associated handler method for the provided request.

	  * @param request the HttpServletRequest
	  * @param response the HttpServletResponse
	 */
	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try {
			InvokeHandler(request, response, HttpMethod.GET);
		}
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}
	
	/**
	  * this method searches recursively for the root cause of an exception
	  *
	  * @param exception the thrown exception
	 */
	private Throwable GetRootCause(Throwable exception)
	{
		while(exception.getCause() != null)
		{
			exception = exception.getCause();
		}
		
		return exception;
	}

	/**
	  * This method is called from the "doPost" method of a HttpServlet.
	  * It calls the associated handler method for the provided request.

	  * @param request the HttpServletRequest
	  * @param response the HttpServletResponse
	 */
	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InvokeHandler(request, response, HttpMethod.POST);
		}
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}

	/**
	  * This method catches and handles all exceptions that are thrown upon invocation of a handler method.
	  * It searches for the root cause of an exception and handles it accordingly. If an exception type is not known,
	  * a ServletException is thrown that is then handled by the calling HttpServlet.
	  *
	  * @param response the HttpServletResponse
	  * @param exception the Exception that is thrown by a handler method
	 */
	private void HandleExceptions(HttpServletResponse response, Exception exception) throws IOException, ServletException {
		Throwable rootCause = GetRootCause(exception);
		if(rootCause instanceof NotFoundException){
			exception.printStackTrace();
			response.setStatus(404);
			response.getWriter().append(rootCause.getMessage());
		}else if(rootCause instanceof UnsupportedOperationException)
		{
			exception.printStackTrace();
			response.setStatus(405);
			response.getWriter().append(rootCause.getMessage());
		}
		else if(rootCause instanceof JsonSyntaxException){
			response.setStatus(400);
			response.getWriter().append("Malformed Json: " + rootCause.getMessage());
		}
		else if(rootCause instanceof ValidationException){
			response.setStatus(422);
			response.getWriter().append(rootCause.getMessage());
		}else if(rootCause instanceof AuthenticationException)
		{
			response.setStatus(403);
			response.getWriter().append(rootCause.getMessage());
		}else if(rootCause instanceof AuthorizationException)
		{
			response.setStatus(403);
			response.getWriter().append(rootCause.getMessage());
		}
		else{
			exception.printStackTrace();
			throw new ServletException(exception);
		}
	}
	
	/**
	  * This method is called from the "doPut" method of a HttpServlet.
	  * It calls the associated handler method for the provided request.

	  * @param request the HttpServletRequest
	  * @param response the HttpServletResponse
	 */
	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InvokeHandler(request, response, HttpMethod.PUT);
		} 
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}

	/**
	  * This method is called from the "doDelete" method of a HttpServlet.
	  * It calls the associated handler method for the provided request.

	  * @param request the HttpServletRequest
	  * @param response the HttpServletResponse
	 */
	@Override
	public void Delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InvokeHandler(request, response, HttpMethod.DELETE);
		}
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}
	
	private HandlerInfo GetHandlerEntry(String path, HttpMethod method) {
		
		for (HandlerInfo handler : _handlers) {
			if (handler.getUriTemplate().Match(path) != null && handler.GetHttpMethod() == method)
				return handler;
		}
		return null;
	}
	
	/**
	  * This method is called from the "doGet", "doPost", "doPut" and "doDelete" methods.
	  * It searches for a handler registered in the _hanlders list that matches the invoked path and http method. If no such handler is found,
	  * a NotFoundException is thrown.
	  * If a handler exists, the parameters of the path are parsed and the handler method is invoked. The result of the handler method (if any)
	  * gets deserialized and written into the response stream.
	  *
	  * It calls the associated handler method for the provided request.
	  * @param request the HttpServletRequest
	  * @param response the HttpServletResponse
	  * @param method the http method of the request
	 */
	private void InvokeHandler(HttpServletRequest request, HttpServletResponse response, HttpMethod method) throws Exception
	{
		Map<String, String[]> map = request.getParameterMap();
		String path = request.getPathInfo();
		HandlerInfo handler = GetHandlerEntry(path, method);
		if(handler == null){
			throw new NotFoundException("ressource '" + path + "' does not exist");
		}
		
		Map<String, String> params = handler.getUriTemplate().Match(path);
		List<Object> parameters = new ArrayList<>();
		
		//set application scope variable if exists
		String application = params.get("application_id");
		Integer applicationId = application == null ? null : Integer.valueOf(application);
        request.setAttribute(
	             Key.get(Integer.class, Names.named("application")).toString(),
	             applicationId);
		
		//Do token authentication if needed
        Authenticate auth = handler.getAuth();
        if(auth != null)
        {
    		Class<? extends ITokenAuthenticationService> authServiceClazz = auth.service();
			ITokenAuthenticationService authService = _injector.getInstance(authServiceClazz);
			if(!authService.Authenticate(request, auth))
				throw new AuthenticationException("User is not authorized for the provided operation");
        }

        //set language scope variable if exists
		String language = params.get("lang");
		if(language != null)
		{
	        request.setAttribute(
		             Key.get(String.class, Names.named("language")).toString(),
		             language);
		}
		
		//Parse the parameters of the path
		for(Entry<String, Parameter> pathParam : handler.getPathParameters().entrySet())
		{
			if(params.containsKey(pathParam.getKey()))
			{
				Parameter methodParam = pathParam.getValue();
				if(methodParam.getType().equals(String.class))
				{
					parameters.add(params.get(pathParam.getKey()));
				}else{
					Object parameterObject = _parserMap.get(methodParam.getType()).invoke(null, params.get(pathParam.getKey()));
					parameters.add(parameterObject);
				}

			}else{
				parameters.add(null);
			}
		}
		
		Object instance = _injector.getInstance(handler.getHandlerClass());
		
		ISerializationService serializer = null;
		
		//Deserialize request payload
		if(handler.getSerializedParameterClass() != void.class)
		{
			serializer = _injector.getInstance(_serializerMap.get(handler.getSerializedParameterClass()));
			if(method == HttpMethod.POST || method == HttpMethod.PUT){
				if(serializer != null)
				{
					Object object = serializer.Deserialize(request);
					parameters.add(object);
				}else{
					parameters.add(GetRequestContent(request));
				}
			}
		}
		
		Object result = handler.getMethod().invoke(instance, parameters.toArray());
		
		//write deserialized object of handler into response stream
		if(result != null)
		{
			if(serializer != null)
			{
				response.getWriter().append(serializer.Serialize(result));
			}else{
				response.getWriter().append(result.toString());
			}
		}
	}
	
	private String GetRequestContent(HttpServletRequest request) throws Exception
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
			throw new Exception(ex);
		}
		return buffer.toString();
	}
}
