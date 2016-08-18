package ch.uzh.ifi.feedback.library.transaction;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TransactionManager {

    public static void withTransaction(Transaction transaction) throws Exception {

        Connection dbConnection = createDatabaseConnection();
        dbConnection.setAutoCommit(false);

        try {

            System.out.println("Starting transaction");
            transaction.execute(dbConnection);


            System.out.println("Committing transaction");
            dbConnection.commit();

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("Rolling back...");
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
    	return null;
    }
}

