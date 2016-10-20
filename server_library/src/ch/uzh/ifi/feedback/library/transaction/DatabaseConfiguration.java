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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;
import org.apache.tomcat.jni.Time;

import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.test.ServletTest;
import ch.uzh.ifi.feedback.library.test.dumps.TestHelper;

public abstract class DatabaseConfiguration implements IDatabaseConfiguration {
	
	private String dbUser;
	private String dbPassword;
	private String testDatabaseDumpFile;

	protected Map<String, String> properties;
	
	public DatabaseConfiguration()
	{
		properties = new HashMap<>();
		ReadConfig();
	}
	
	public void StartDebugMode()
	{
    	CreateDumps();
	}
	
	public void RestoreTestDatabase()
	{
        //Restore Databases from dump file
        String restoreTestDbCmd = String.format("mysql -u %s -p%s %s < %s", dbUser, dbPassword, getTestDatabase(), testDatabaseDumpFile);
        try {
        	if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC)
        	{
				Runtime.getRuntime().exec(new String[]{"bash", "-c", restoreTestDbCmd}).waitFor();
        	}else if(SystemUtils.IS_OS_WINDOWS)
        	{
				Runtime.getRuntime().exec(new String[]{"cmd","/c", restoreTestDbCmd}).waitFor();
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
	
	private void CreateDumps()
	{
		InputStream inputStream = getTestDatabaseDump();
		testDatabaseDumpFile = generateTempFile(inputStream, "dump_" + getTestDatabase());
	}
	
	protected String generateTempFile(InputStream input, String filename)
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

	public abstract String getDatabase();

	public abstract String getTestDatabase();
	
	protected abstract InputStream getTestDatabaseDump();
}
