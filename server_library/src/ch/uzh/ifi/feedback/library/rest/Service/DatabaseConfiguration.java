package ch.uzh.ifi.feedback.library.rest.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

public class DatabaseConfiguration {
	
	private String repositoryDb;
	private String orchestratorDb;
	private String databaseDirectory;
	private String repositoryDbTest;
	private String orchestratorDbTest;
	private String configurationFile;
	
	public DatabaseConfiguration()
	{
		ReadConfig();
	}
	
	public void ReadConfig()
	{
		Properties metaprop = new Properties();
    	try {
    		InputStream propertiesStream = TransactionManager.class.getResourceAsStream("config.properties");
    		metaprop.load(propertiesStream);
    		configurationFile = metaprop.getProperty("dbConfigFile");
    	
    		Properties prop = new Properties();
    		File f = new File(configurationFile);
    		propertiesStream = new FileInputStream(f);
    		prop.load(propertiesStream);
    		repositoryDb = prop.getProperty("repository_db");
    		orchestratorDb = prop.getProperty("orchestrator_db");
    		databaseDirectory = prop.getProperty("database_directory");
    		repositoryDbTest = prop.getProperty("repository_test_db");
    		orchestratorDbTest = prop.getProperty("orchestrator_test_db");
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} 
	}
	
	public void WriteConfig()
	{
	    try {
	        Properties props = new Properties();
	        props.setProperty("repository_db", repositoryDb);
	        props.setProperty("orchestrator_db", orchestratorDb);
	        props.setProperty("database_directory", databaseDirectory);
	        props.setProperty("repository_test_db", repositoryDbTest);
	        props.setProperty("orchestrator_test_db", orchestratorDbTest);
	        File f = new File(configurationFile);
	        OutputStream out = new FileOutputStream( f );
	        props.store(out, "");
	        out.close();
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}
	
	public String getRepositoryDb() {
		return repositoryDb;
	}

	public void setRepositoryDb(String repositoryDb) {
		this.repositoryDb = repositoryDb;
	}

	public String getOrchestratorDb() {
		return orchestratorDb;
	}

	public void setOrchestratorDb(String orchestratorDb) {
		this.orchestratorDb = orchestratorDb;
	}

	public String getDatabaseDirectory() {
		return databaseDirectory;
	}

	public void setDatabaseDirectory(String databaseDirectory) {
		this.databaseDirectory = databaseDirectory;
	}

	public String getRepositoryDbTest() {
		return repositoryDbTest;
	}

	public void setRepositoryDbTest(String repositoryDbTest) {
		this.repositoryDbTest = repositoryDbTest;
	}

	public String getOrchestratorDbTest() {
		return orchestratorDbTest;
	}

	public void setOrchestratorDbTest(String orchestratorDbTest) {
		this.orchestratorDbTest = orchestratorDbTest;
	}

}
