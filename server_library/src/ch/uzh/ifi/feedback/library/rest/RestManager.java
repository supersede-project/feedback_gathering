package ch.uzh.ifi.feedback.library.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.AlreadyBoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.el.MethodNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.reflections.*;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.gson.JsonSyntaxException;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ch.uzh.ifi.feedback.library.rest.Routing.HandlerInfo;
import ch.uzh.ifi.feedback.library.rest.Routing.HttpMethod;
import ch.uzh.ifi.feedback.library.rest.Routing.UriTemplate;
import ch.uzh.ifi.feedback.library.rest.Service.IDbItem;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import ch.uzh.ifi.feedback.library.rest.validation.Validate;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidatorBase;
import javassist.NotFoundException;


public class RestManager implements IRestManager {

	private List<HandlerInfo> _handlers;
	private Map<Class<?>, Method> _parserMap;
	private Map<Class<?>, ISerializationService<?>> _serializerMap;
	private Map<Class<?>, ValidatorBase<?>> _validatorMap;
	private Injector _injector;

	public RestManager() {
		_handlers = new ArrayList<>();
		_parserMap = new HashMap<>();
		_validatorMap = new HashMap<>();
		_serializerMap = new HashMap<>();
	}

	public void Init(String packageName) throws Exception {
		
		InitParserMap();
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
		
		InitInjector(reflections);
		InitHandlerTable(reflections);
	}
	
	private void InitInjector(Reflections reflections) throws Exception
	{
		Set<Class<? extends AbstractModule>> modules = reflections.getSubTypesOf(AbstractModule.class);
		List<AbstractModule> moduleInstances = new ArrayList<>();
		for(Class<? extends AbstractModule> moduleClazz : modules)
		{
			moduleInstances.add(moduleClazz.newInstance());
		}
		_injector = Guice.createInjector(moduleInstances);
	}
	
	private void InitHandlerTable(Reflections reflections) throws Exception
	{
		Set<Class<?>> controllerAnnotated = reflections.getTypesAnnotatedWith(Controller.class);
		for(Class<?> clazz : controllerAnnotated){
			Class<?> parameterClass = clazz.getAnnotation(Controller.class).value();
			
			if(parameterClass.isAnnotationPresent(Validate.class))
			{
				Validate annotation = parameterClass.getAnnotation(Validate.class);
				Class<? extends ValidatorBase<?>> validatorClass = parameterClass.getAnnotation(Validate.class).value();
				ValidatorBase<?> validator = _injector.getInstance(validatorClass);
				_validatorMap.put(parameterClass, validator);
			}
			
			if(parameterClass.isAnnotationPresent(Serialize.class))
			{
				Class<? extends ISerializationService<?>> serializerClass = parameterClass.getAnnotation(Serialize.class).value();
				ISerializationService<?> serializer = _injector.getInstance(serializerClass);
				_serializerMap.put(parameterClass, serializer);
			}
			
			for(Method m : clazz.getMethods()){
				if(m.isAnnotationPresent(Path.class)){
					String path = "{lang}/" + m.getAnnotation(Path.class).value();
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
						throw new AlreadyBoundException("handler for template '" + path + "' already registered!/n");
				
					HandlerInfo info = new HandlerInfo(m, method, clazz, parameterClass, template);
					
					Parameter[] params = m.getParameters();
					for(int i = 0; i < params.length; i++){
						if (params[i].isAnnotationPresent(PathParam.class)){
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

	private void InitParserMap() throws Exception {
		
			_parserMap.put(Integer.class, Integer.class.getMethod("parseInt", String.class));
			_parserMap.put(Double.class, Double.class.getMethod("parseDouble", String.class));
			_parserMap.put(Long.class, Long.class.getMethod("parseLong", String.class));
			_parserMap.put(Float.class, Float.class.getMethod("parseFloat", String.class));
			_parserMap.put(Timestamp.class, Timestamp.class.getMethod("valueOf", String.class));
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try {
			InvokeHandler(request, response, HttpMethod.GET);
		}
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}
	
	private Throwable GetRootCause(Throwable ex)
	{
		while(ex.getCause() != null)
		{
			ex = ex.getCause();
		}
		
		return ex;
	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InvokeHandler(request, response, HttpMethod.POST);

		}
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}

	private void HandleExceptions(HttpServletResponse response, Exception ex) throws IOException, ServletException {
		Throwable rootCause = GetRootCause(ex);
		if(rootCause instanceof NotFoundException){
			ex.printStackTrace();
			response.setStatus(404);
			response.getWriter().append(rootCause.getMessage());
		}else if(rootCause instanceof UnsupportedOperationException)
		{
			ex.printStackTrace();
			response.setStatus(405);
			response.getWriter().append(rootCause.getMessage());
		}
		else if(rootCause instanceof JsonSyntaxException){
			response.setStatus(400);
			response.getWriter().append("Malformed Json: " + ex.getMessage());
		}
		else{
			ex.printStackTrace();
			throw new ServletException(ex);
		}
	}
	
	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			InvokeHandler(request, response, HttpMethod.PUT);

		} 
		catch(Exception ex){
			HandleExceptions(response, ex);
		}
	}

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
	
	private void InvokeHandler(HttpServletRequest request, HttpServletResponse response, HttpMethod method) throws Exception
	{
		Map<String, String[]> map = request.getParameterMap();
		String path = request.getServletPath();
		HandlerInfo handler = GetHandlerEntry(path, method);
		if(handler == null){
			throw new NotFoundException("ressource '" + path + "' does not exist");
		}
		
		Map<String, String> params = handler.getUriTemplate().Match(path);
		List<Object> parameters = new ArrayList<>();
		String language = params.get("lang");
		
		for(Entry<String, Parameter> pathParam : handler.getPathParameters().entrySet())
		{
			if(params.containsKey(pathParam.getKey()))
			{
				Parameter methodParam = pathParam.getValue();
				Object parameterObject = _parserMap.get(methodParam.getType()).invoke(null, params.get(pathParam.getKey()));
				parameters.add(parameterObject);
			}else{
				parameters.add(null);
			}
		}
		
		Object instance = _injector.getInstance(handler.getHandlerClass());
		
		ISerializationService serializer = _serializerMap.get(handler.getSerializedParameterClass());
		if(method == HttpMethod.POST || method == HttpMethod.PUT){
			if(serializer != null)
			{
				Object object = serializer.Deserialize(request);
				ValidatorBase validator = _validatorMap.get(handler.getSerializedParameterClass());
				if(validator != null)
				{
					if(object instanceof IDbItem<?>)
					{
						if(method == HttpMethod.PUT)
						{
							//Merge object with original on update
							object = validator.Merge((IDbItem)object);
						}
						ValidationResult result = validator.Validate((IDbItem<?>)object);
						if(result.hasErrors())
						{
							response.setStatus(422);
							response.getWriter().append(serializer.Serialize(result.GetValidationErrors()));
							return;
						}
						
					}else{
						//Handle error when object not instance of IdbItem...
					}
				}
				parameters.add(object);
			}else{
				parameters.add(GetRequestContent(request));
			}
		}
		
		if (instance instanceof RestController<?>)
			((RestController<?>)instance).SetLanguage(language);
		
		Object result = handler.getMethod().invoke(instance, parameters.toArray());
		
		if(result != null){
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
