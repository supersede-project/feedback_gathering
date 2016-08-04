package ch.uzh.ifi.feedback.library.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import javax.el.MethodNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sound.midi.ControllerEventListener;

import org.reflections.*;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.transaction.TransactionManager;
import javassist.NotFoundException;


public class RestManager implements IRestManager {
	/*
	private Map<UriTemplate, Class<RestController>> _routingMap;
	private Map<Type, Method> _parsingMap;
	*/
	private List<HandlerInfo> _handlers;
	private Map<Class<? extends ISerializationService<?>>, ISerializationService<?>> _serializationMap;
	private Map<Class<?>, Method> _parserMap;

	public RestManager() {
		/*
		_routingMap = new HashMap<>();
		_parsingMap = new HashMap<>();
		*/
		_serializationMap = new HashMap<>();
		_handlers = new ArrayList<>();
		_parserMap = new HashMap<>();
	}

	public void Init(String packageName) throws Exception {
		
		InitParserMap();
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
		
		Set<Class<?>> controllerAnnotated = reflections.getTypesAnnotatedWith(Controller.class);
		for(Class<?> clazz : controllerAnnotated){
			Object instance = clazz.newInstance();
			for(Method m : clazz.getMethods()){
				if(m.isAnnotationPresent(Path.class)){
					String path = m.getAnnotation(Path.class).value();
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
						throw new Exception("handler for template " + path + " already registered!/n");
					
					if(!m.isAnnotationPresent(Serialize.class))
						throw new Exception("Handler method " + m.getName() + " needs a serialization annotation");
					
					HandlerInfo info = new HandlerInfo(m, method, instance, template);
					
					Class<? extends ISerializationService<?>> serializerClass = m.getAnnotation(Serialize.class).value();
					if(!_serializationMap.containsKey(serializerClass))
						_serializationMap.put(serializerClass, serializerClass.newInstance());
					info.setSerializationClass(serializerClass);
					
					Parameter[] params = m.getParameters();
					for(int i = 0; i < params.length; i++){
						if (params[i].isAnnotationPresent(PathParam.class)){
							String paramName = params[i].getAnnotation(PathParam.class).value();
							info.getPathParameters().put(paramName, params[i]);
						}else if(i < params.length - 1){
							throw new Exception("No annotation for parameter " + params[i].getName() + " present!/n");
						}else{
							Class<?> paramClazz = params[i].getType();
							
							//Todo:Check return type of serializer to match with parameter type
							info.setSerializedParameterClass(paramClazz);
						}
					}
					
					if(info.getSerializedParameterClass() == null){
						if (!m.getReturnType().equals(Void.TYPE)){
							Class<?> returnType = m.getReturnType();
							info.setSerializedParameterClass(returnType);
						}
					}
					_handlers.add(info);
				}
			}
		}

		/*
		for(Class<?> c : controllerAnnotated){
			
			if(!(RestController.class.isAssignableFrom(c))){
				throw new Exception("Every controller class must be derived from the generic superclass 'RestController'");
			}
			
			Controller controllerAnnotation = c.getAnnotation(Controller.class);
			for(String route : controllerAnnotation.Routes())
			{
				_routingMap.put(UriTemplate.Parse(route), (Class<RestController>) c);
			}
		}*/
	}
	
	private void InitParserMap() throws Exception {
		
		try{
			_parserMap.put(Integer.class, Integer.class.getMethod("parseInt", String.class));
			_parserMap.put(Double.class, Double.class.getMethod("parseDouble", String.class));
			_parserMap.put(Long.class, Long.class.getMethod("parseLong", String.class));
			_parserMap.put(Float.class, Float.class.getMethod("parseFloat", String.class));
			_parserMap.put(Timestamp.class, Timestamp.class.getMethod("valueOf", String.class));
		}catch(Exception e){
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
			InvokeHandler(request, response, HttpMethod.GET);
			/*
			IRestController controller = GetController(request);
			Object result = controller.Get(request, response);
			String output = controller.getSerializationService().Serialize(result);
			response.getWriter().append(output);
			*/

		} catch (Exception ex) {
			throw new ServletException(ex);
		}

	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			InvokeHandler(request, response, HttpMethod.POST);
			/*
			RestController controller = GetController(request);
			Object result = controller.getSerializationService().Deserialize(GetRequestContent(request));
			controller.Post(request, response, result);
			*/

		} catch(JsonSyntaxException ex)
		{
			response.setStatus(400);
			try {
				response.getWriter().append("Malformed Json");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(NotFoundException ex)
		{
			response.setStatus(404);
			try {
				response.getWriter().append("Not Found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			InvokeHandler(request, response, HttpMethod.PUT);
			/*
			IRestController controller = GetController(request);
			Object result = controller.getSerializationService().Deserialize(GetRequestContent(request));
			controller.Put(request, response, result);*/

		} catch(NullPointerException ex)
		{
			response.setStatus(404);
			try {
				response.getWriter().append("Not Found");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	@Override
	public void Delete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		throw new MethodNotFoundException();
	}

	/*
	private Entry<UriTemplate, Class<RestController>> GetClassEntry(String path) {
	
		for (Entry<UriTemplate, Class<RestController>> entry : _routingMap.entrySet()) {
			if (entry.getKey().Match(path) != null)
				return entry;
		}

		return null;
	}*/
	
	private HandlerInfo GetHandlerEntry(String path, HttpMethod method) {
		
		for (HandlerInfo handler : _handlers) {
			if (handler.getUriTemplate().Match(path) != null && handler.GetHttpMethod() == method)
				return handler;
		}

		return null;
	}
	
	private void InvokeHandler(HttpServletRequest request, HttpServletResponse response, HttpMethod method) throws Exception
	{
		String path = request.getServletPath();
		HandlerInfo handler = GetHandlerEntry(path, method);
		if(handler == null){
			response.setStatus(404);
			return;
		}
		
		Map<String, String> params = handler.getUriTemplate().Match(path);
		List<Object> parameters = new ArrayList<>();
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
		
		ISerializationService serializer = _serializationMap.get(handler.getSerializationClass());
		if(method == HttpMethod.POST || method == HttpMethod.PUT){
			String content = GetRequestContent(request);
			parameters.add(serializer.Deserialize(content));
		}
		
		Object result = handler.getMethod().invoke(handler.getHandlerInstance(), parameters.toArray());
		
		if(result != null){
			response.getWriter().append(serializer.Serialize(result));
		}
	}
	
	/*
	private RestController GetController(HttpServletRequest request) throws InstantiationException, IllegalAccessException, NotFoundException, InvocationTargetException, NoSuchMethodException
	{
		String path = request.getServletPath();
		Entry<UriTemplate, Class<RestController>> classEntry = GetClassEntry(path);
		if(classEntry == null)
			throw new NotFoundException("No controller registered");
		
		request = SetAttributes(request, path, classEntry.getKey());
		Class<RestController> controllerClass = classEntry.getValue();

		return controllerClass.newInstance();
	}*/
	
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

	/*
	private HttpServletRequest SetAttributes(HttpServletRequest request, String path, UriTemplate template)
	{
		Map<String, String> params = template.Match(path);
		params.forEach((k, v) -> {
			request.setAttribute(k, v);
		});
		
		return request;
	}*/
}
