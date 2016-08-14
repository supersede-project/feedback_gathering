package ch.uzh.ifi.feedback.repository.serialization;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.uzh.ifi.feedback.library.rest.serialization.DefaultSerializer;

public abstract class RepositorySerializationService<T> extends DefaultSerializer<T> {
	
	@Override
	public T Deserialize(HttpServletRequest request) {
		
		String data = request.getParameter("json");
		if (data == null)
		{
			data = GetRequestContent(request);
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd hh:mm:ss.S").create();
		T requestObject = gson.fromJson(data, serializationType);
		return requestObject;
	}
	
	private String GetRequestContent(HttpServletRequest request)
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
			ex.printStackTrace();
		}
		return buffer.toString();
	}
	
}
