package ch.uzh.ifi.feedback.library.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class UriTemplate {
	
	private Map<TemplatePart, String> _map;
	
	private UriTemplate(Map<TemplatePart, String> map)
	{
		_map = map;
	}
	
	public Map<String, String> Match(String path)
	{
		String[] parts = path.split("/");
		List<String> validParts = new ArrayList<>();
		for(String part : parts)
		{
			part = part.trim();
			if(part.isEmpty())
				continue;
			
			validParts.add(part);
		}
		
		if(validParts.size() != _map.size())
			return null;
		
		Map<String, String> variables = new HashMap<>();
		Set<Entry<TemplatePart, String>> set = _map.entrySet();
		Iterator<Entry<TemplatePart, String>> iterator = set.iterator();
		
		for(String part : validParts){
			
			Entry<TemplatePart, String> entry = iterator.next();
			String value = entry.getValue();
			if(entry.getKey().equals(TemplatePart.Literal) && !part.equals(value))
				return null;
			
			if(entry.getKey().equals(TemplatePart.Variable))
				variables.put(entry.getValue(), part);
			
		}
		
		return variables;
	}
	
	public static UriTemplate Parse(String path)
	{
		Map<TemplatePart, String> map = new LinkedHashMap<>();
		String[] parts = path.split("/");
		
		for(String part : parts){		
			part = part.trim();
			
			if(part.isEmpty())
				continue;
			
			if(part.startsWith("{") && part.endsWith("}")){
				map.put(TemplatePart.Variable, part.substring(1, part.length()-1));

			}else{
				map.put(TemplatePart.Literal, part);	
			}	
		}
		return new UriTemplate(map);
	}
}

