package ch.uzh.ifi.feedback.library.rest.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import ch.uzh.ifi.feedback.library.transaction.TransactionManager;

public class DatabaseConfiguration {
	
	private String repositoryDb;
	private String orchestratorDb;
	private String repositoryDbTemp;
	private String orchestratorDbTemp;
	private String repositoryDbTest;
	private String orchestratorDbTest;
	private String configurationFile;
	private String repositoryDumpPath;
	private String orchestratorDumpPath;
	private String dbUser;
	private String dbPassword;
	
	public DatabaseConfiguration()
	{
		ReadConfig();
	}
	
	public void UseTestConfiguration()
	{
        repositoryDb = repositoryDbTest;
        orchestratorDb = orchestratorDbTest;
        WriteConfig();
	}
	
	public void RestoreConfiguration()
	{
        repositoryDb = repositoryDbTemp;
        orchestratorDb = orchestratorDbTemp;
        
        //Restore Databases from dump files
        String restoreRepositoryCmd = String.format("mysql -u %s -p%s %s < %s", dbUser, dbPassword, repositoryDbTest, repositoryDumpPath);
        String restoreOrchestratorCmd = String.format("mysql -u %s -p%s %s < %s", dbUser, dbPassword, orchestratorDbTest, orchestratorDumpPath);
        try {
        	if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
        	{
				Runtime.getRuntime().exec(new String[]{"bash","-c", restoreRepositoryCmd}).waitFor();
				Runtime.getRuntime().exec(new String[]{"bash", "-c", restoreOrchestratorCmd}).waitFor();
        	}else if(SystemUtils.IS_OS_WINDOWS)
        	{
				Runtime.getRuntime().exec(new String[]{"cmd","/c", restoreRepositoryCmd}).waitFor();
				Runtime.getRuntime().exec(new String[]{"cmd","/c", restoreOrchestratorCmd}).waitFor();
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        WriteConfig();
	}
	
	private void ReadConfig()
	{
		Properties prop = new Properties();
    	try {
    		InputStream propertiesStream = TransactionManager.class.getResourceAsStream("config.properties");
    		prop.load(propertiesStream);
    		configurationFile = prop.getProperty("dbConfigFile");
    		dbUser = prop.getProperty("dbuser");
    		dbPassword = prop.getProperty("dbpassword");
    		
    		File configFile = new File(configurationFile);
    		propertiesStream = new FileInputStream(configFile);
    		prop.load(propertiesStream);
    		repositoryDb = prop.getProperty("repository_db");
    		orchestratorDb = prop.getProperty("orchestrator_db");
    		repositoryDbTemp = repositoryDb;
    		orchestratorDbTemp = orchestratorDb;
    		repositoryDumpPath = prop.getProperty("repository_test_dump");
    		orchestratorDumpPath = prop.getProperty("orchestrator_test_dump");
    		repositoryDbTest = prop.getProperty("repository_test_db");
    		orchestratorDbTest = prop.getProperty("orchestrator_test_db");
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} 
	}
	
	private void WriteConfig()
	{
	    try {
	        Properties props = new Properties();
	        props.setProperty("repository_db", repositoryDb);
	        props.setProperty("orchestrator_db", orchestratorDb);
	        props.setProperty("orchestrator_test_dump", orchestratorDumpPath);
	        props.setProperty("repository_test_dump", repositoryDumpPath);
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

	public String getRepositoryDumpPath() {
		return repositoryDumpPath;
	}

	public void setRepositoryDumpPath(String repositoryDumpPath) {
		this.repositoryDumpPath = repositoryDumpPath;
	}

	public String getOrchestratorDumpPath() {
		return orchestratorDumpPath;
	}

	public void setOrchestratorDumpPath(String orchestratorDumpPath) {
		this.orchestratorDumpPath = orchestratorDumpPath;
	}

}
