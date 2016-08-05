package ch.uzh.ifi.feedback.library.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.el.MethodNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.PUT;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.library.rest.annotations.Serialize;
import ch.uzh.ifi.feedback.library.rest.serialization.ISerializationService;
import javassist.NotFoundException;


public class RestManager implements IRestManager {

	private List<HandlerInfo> _handlers;
	private Map<Class<?>, Method> _parserMap;
	private Injector _injector;

	public RestManager() {
		_handlers = new ArrayList<>();
		_parserMap = new HashMap<>();
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
			if(!RestController.class.isAssignableFrom(clazz))
				throw new Exception("Class " + clazz.getName() + " must derive from RestController!");
			
			RestController<?> instance = (RestController<?>) _injector.getInstance(clazz);
			
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
				
					HandlerInfo info = new HandlerInfo(m, method, instance, template);
					
					Parameter[] params = m.getParameters();
					for(int i = 0; i < params.length; i++){
						if (params[i].isAnnotationPresent(PathParam.class)){
							String paramName = params[i].getAnnotation(PathParam.class).value();
							info.getPathParameters().put(paramName, params[i]);
						}else if(i < params.length - 1){
							throw new Exception("No annotation for parameter " + params[i].getName() + " present!/n");
						}else{
							Class<?> paramClazz = params[i].getType();
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

		} catch (Exception ex) {
			throw new ServletException(ex);
		}

	}

	@Override
	public void Post(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			InvokeHandler(request, response, HttpMethod.POST);

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
		
		ISerializationService serializer = handler.getHandlerInstance().getSerializationService();
		if(method == HttpMethod.POST || method == HttpMethod.PUT){
			String content = GetRequestContent(request);
			parameters.add(serializer.Deserialize(content));
		}
		
		Object result = handler.getMethod().invoke(handler.getHandlerInstance(), parameters.toArray());
		
		if(result != null){
			response.getWriter().append(serializer.Serialize(result));
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
