package ch.uzh.ifi.feedback.library.transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.library.test.dumps.TestHelper;

@Singleton
public class DatabaseConfiguration implements IDatabaseConfiguration {
	
	private String repositoryDb;
	private String orchestratorDb;
	private String repositoryDbTest;
	private String orchestratorDbTest;
	private String dbUser;
	private String dbPassword;
	private String orchestratorDumpFile;
	private String repositoryDumpFile;
	
	public DatabaseConfiguration()
	{
		ReadConfig();
    	CreateDumps();
	}
	
	public void StartDebugMode()
	{
		repositoryDb = repositoryDbTest;
		orchestratorDb = orchestratorDbTest;
	}
	
	public void RestoreTestDatabases()
	{
        //Restore Databases from dump files
        String restoreRepositoryCmd = String.format("mysql -u %s -p%s %s < %s", dbUser, dbPassword, repositoryDbTest, repositoryDumpFile);
        String restoreOrchestratorCmd = String.format("mysql -u %s -p%s %s < %s", dbUser, dbPassword, orchestratorDbTest, orchestratorDumpFile);
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
       
	}
	
	private void ReadConfig()
	{
		Properties prop = new Properties();
    	try {
    		InputStream propertiesStream = TransactionManager.class.getResourceAsStream("config.properties");
    		prop.load(propertiesStream);
    		dbUser = prop.getProperty("dbuser");
    		dbPassword = prop.getProperty("dbpassword");
    		repositoryDb = prop.getProperty("repositoryDb");
    		orchestratorDb = prop.getProperty("orchestratorDb");
    		repositoryDbTest = prop.getProperty("repositoryTestDb");
    		orchestratorDbTest = prop.getProperty("orchestratorTestDb");
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
	}
	
	private void CreateDumps()
	{
		InputStream orchestratorInputStream = this.getClass().getResourceAsStream("orchestrator_test_dump.sql");
		InputStream repositoryInputStream = this.getClass().getResourceAsStream("repository_test_dump.sql");
		
		orchestratorDumpFile = generateTempFile(orchestratorInputStream, "orchestratorDump");
		repositoryDumpFile = generateTempFile(repositoryInputStream, "repositorDump");
	}
	
	private String generateTempFile(InputStream input, String filename)
	{
        try {
            File file = File.createTempFile(filename, ".tmp");
            OutputStream out = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            file.deleteOnExit();
            
            return file.getAbsolutePath();            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
	}
	
	public String getRepositoryDb() {
		return repositoryDb;
	}

	public String getOrchestratorDb() {
		return orchestratorDb;
	}

	public String getRepositoryDbTest() {
		return repositoryDbTest;
	}

	public String getOrchestratorDbTest() {
		return orchestratorDbTest;
	}
}
