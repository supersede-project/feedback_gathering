package ch.uzh.ifi.feedback.library.transaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class stores the database configuration for a server. 
 * This includes the userName, password, the database name and the test database name.
 * 
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public abstract class DatabaseConfiguration implements IDatabaseConfiguration {
	
	private String dbUser;
	private String dbPassword;

	protected Map<String, String> properties;
	
	public DatabaseConfiguration()
	{
		properties = new HashMap<>();
		ReadConfig();
	}
	
	public abstract void StartDebugMode();
	
	private void ReadConfig()
	{
		Properties prop = new Properties();
    	try {
    		InputStream propertiesStream = TransactionManager.class.getResourceAsStream("config.properties");
    		prop.load(propertiesStream);
    		for(Object k : prop.keySet())
    		{
    			String key = (String)k;
    			properties.put(key, prop.getProperty(key));
    		}
    		dbUser = prop.getProperty("dbuser");
    		dbPassword = prop.getProperty("dbpassword");
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
	}
	
	public abstract String getDatabase();

	public abstract String getTestDatabase();
	
	@Override
	public String getUserName()
	{
		return dbUser;
	}
	
	@Override
	public String getPassword()
	{
		return dbPassword;
	}
}
