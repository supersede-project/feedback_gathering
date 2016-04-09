package ch.uzh.ifi.feedback.library.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.*;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;


public class RestManager implements IRestManager {
	
	private Map<UriTemplate, Class<RestController>> _routingMap;

	public RestManager() {
		
		_routingMap = new HashMap<>();
	}

	public void Init(String packageName) throws Exception {
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
		
		Set<Class<?>> controllerAnnotated = reflections.getTypesAnnotatedWith(Controller.class);

		for(Class<?> c : controllerAnnotated){
			
			if(!(RestController.class.isAssignableFrom(c))){
				throw new Exception("Every controller class must be derived from the generic superclass 'RestController'");
			}
			
			Controller controllerAnnotation = c.getAnnotation(Controller.class);
			_routingMap.put(UriTemplate.Parse(controllerAnnotation.Route()), (Class<RestController>) c);
		}
	}

	@Override
	public void Get(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		try {
			String path = request.getServletPath();
			Entry<UriTemplate, Class<RestController>> classEntry = GetClassEntry(path);
			request = SetAttributes(request, path, classEntry.getKey());
			Class<RestController> controllerClass = classEntry.getValue();
			IRestController controller = controllerClass.newInstance();
			Object result = controller.Get(request, response);
			String output = controller.Serialize(result);
			response.getWriter().append(output);

		} catch (Exception ex) {
			throw new ServletException(ex);
		}

	}

	@Override
	public void Put(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String path = request.getServletPath();
			Entry<UriTemplate, Class<RestController>> classEntry = GetClassEntry(path);
			request = SetAttributes(request, path, classEntry.getKey());
			Class<RestController> controllerClass = classEntry.getValue();
			IRestController controller = controllerClass.newInstance();
			Object result = controller.Deserialize(GetRequestContent(request));
			controller.Put(request, response, result);

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	@Override
	public void Delete(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {

			Get(request, response);

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	private Entry<UriTemplate, Class<RestController>> GetClassEntry(String path) {

		for (Entry<UriTemplate, Class<RestController>> entry : _routingMap.entrySet()) {
			if (entry.getKey().Match(path) != null)
				return entry;
		}

		return null;
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

	private HttpServletRequest SetAttributes(HttpServletRequest request, String path, UriTemplate template)
	{
		Map<String, String> params = template.Match(path);
		params.forEach((k, v) -> {
			request.setAttribute(k, v);
		});
		
		return request;
	}
}
