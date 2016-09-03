package ch.uzh.ifi.feedback.library.rest.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfiguration {
	
	public static String SelectedConfiguration = "config.properties";
	
	private String repositoryDb;
	private String orchestratorDb;
	
	public DatabaseConfiguration()
	{
        Properties prop = new Properties();
    	InputStream input = null;
    	try {
    		InputStream propertiesStream   = DatabaseConfiguration.class.getResourceAsStream(SelectedConfiguration);
    		prop.load(propertiesStream);
    		repositoryDb = prop.getProperty("repository_db");
    		orchestratorDb = prop.getProperty("orchestrator_db");
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
	}

	public String getRepositoryDb() {
		return repositoryDb;
	}

	public String getOrchestratorDb() {
		return orchestratorDb;
	}
}
