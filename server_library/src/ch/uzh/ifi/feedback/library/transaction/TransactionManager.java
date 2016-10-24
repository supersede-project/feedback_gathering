package ch.uzh.ifi.feedback.library.transaction;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.uzh.ifi.feedback.library.rest.ContextFinalizer;
import javassist.NotFoundException;

public class TransactionManager {

    private static final Log LOGGER = LogFactory.getLog(ContextFinalizer.class);
    
    public static void withTransaction(Transaction transaction) throws SQLException, NotFoundException {

        Connection dbConnection = createDatabaseConnection();
        dbConnection.setAutoCommit(false);

        try {
            LOGGER.info("Starting transaction");
            transaction.execute(dbConnection);

            LOGGER.info("Committing transaction");
            dbConnection.commit();

        } catch (SQLException e) {

        	LOGGER.error(e.getMessage());
            e.printStackTrace();
            LOGGER.error("Rolling back...");
            dbConnection.rollback();
            throw e;
            
        } finally {
            dbConnection.close();
        }
    }

    public static Connection createDatabaseConnection() throws SQLException {

        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
        
        Properties prop = new Properties();
    	InputStream input = null;
    	try {
    		InputStream propertiesStream   = TransactionManager.class.getResourceAsStream("config.properties");
    		prop.load(propertiesStream);
    		String dbUrl = prop.getProperty("dburl");
    		String dbUser = prop.getProperty("dbuser");
    		String dbPassword = prop.getProperty("dbpassword");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    	} catch (IOException ex) {
    		LOGGER.error(ex.getMessage());
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				LOGGER.error(e.getMessage());
    				e.printStackTrace();
    			}
    		}
    	}
    	return null;
    }
}

