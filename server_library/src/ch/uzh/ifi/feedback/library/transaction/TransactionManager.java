package ch.uzh.ifi.feedback.library.transaction;

import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;

public class TransactionManager {

    public void withTransaction(Transaction transaction) throws IOException, Exception {

        Connection dbConnection = createDatabaseConnection();
        dbConnection.setAutoCommit(false);

        try {

            System.out.println("Starting transaction");
            transaction.execute(dbConnection);


            System.out.println("Committing transaction");
            dbConnection.commit();

        } catch (Exception e) {

            System.out.println(e.getMessage());
            System.out.println("Rolling back...");
            dbConnection.rollback();
            throw e;
            
        } finally {
            dbConnection.close();
        }
    }

    public Connection createDatabaseConnection() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
    }
}

