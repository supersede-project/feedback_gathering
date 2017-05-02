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

/**
 * This class is responsible for creating database connections and issuing Transactions against the database.
 *
 * @author Florian Schüpfer
 * @version 1.0
 * @since   2016-11-14
 */
public class TransactionManager {

    private static final Log LOGGER = LogFactory.getLog(TransactionManager.class);
    
    /**
     * This method executes a Transaction lambda expression against the database. When an exception occurs, a rollback is executed
     * to restore the database.
     *
     * @param transaction the transaction to execute against the database
     * @throws SQLException
     * @throws NotFoundException
     */
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

    /**
     * This method creates and returns a Connection object with help of the credentials that are stored in the config.properties file.
     *
     * @return the Connection object
     * @throws SQLException
     */
    public static Connection createDatabaseConnection() throws SQLException {

        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
        
        Properties prop = new Properties();
    	InputStream input = null;
        InputStream propertiesStream = null;

        try {
    		propertiesStream = TransactionManager.class.getResourceAsStream("config.properties");
    		prop.load(propertiesStream);
    		String dbUrl = prop.getProperty("dburl");
    		String dbUser = prop.getProperty("dbuser");
    		String dbPassword = prop.getProperty("dbpassword");

            if(propertiesStream != null) {
                propertiesStream.close();
            }

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

