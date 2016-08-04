package ch.uzh.ifi.feedback.library.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;


public class UriTemplate {
	
	private List<TemplateEntry> _entries;
	private String _path;
	
	private UriTemplate(List<TemplateEntry> entries, String path)
	{
		_entries = entries;
		_path = path;
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
		
		if(validParts.size() != _entries.size())
			return null;
		
		Map<String, String> variables = new HashMap<>();
		
		for(int i = 0; i < validParts.size(); ++i){
			
			String part = validParts.get(i);
			TemplateEntry entry = _entries.get(i);
			String value = entry.getValue();
			if(entry.getTemplatePart().equals(TemplatePart.Literal) && !part.equals(value))
				return null;
			
			if(entry.getTemplatePart().equals(TemplatePart.Variable))
				variables.put(entry.getValue(), part);
		}
		
		return variables;
	}
	
	public static UriTemplate Parse(String path)
	{
		List<TemplateEntry> entries = new LinkedList<>();
		String[] parts = path.split("/");
		
		for(String part : parts){		
			part = part.trim();
			
			if(part.isEmpty())
				continue;
			
			if(part.startsWith("{") && part.endsWith("}")){
				entries.add(new TemplateEntry(TemplatePart.Variable, part.substring(1, part.length()-1)));

			}else{
				entries.add(new TemplateEntry(TemplatePart.Literal, part));
			}	
		}
		return new UriTemplate(entries, path);
	}

	public String get_path() {
		return _path;
	}
}

