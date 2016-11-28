package ch.uzh.ifi.feedback.library.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public abstract class MailConfiguration implements IMailConfiguration {

	protected Map<String, String> properties;
	
	public MailConfiguration()
	{
		properties = new HashMap<>();
		ReadConfig();
	}
	
	private void ReadConfig()
	{
		Properties prop = new Properties();
    	try {
    		InputStream propertiesStream = MailConfiguration.class.getResourceAsStream("config.properties");
    		prop.load(propertiesStream);
    		for(Object k : prop.keySet())
    		{
    			String key = (String)k;
    			properties.put(key, prop.getProperty(key));
    		}

    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
	}
}
