package ch.uzh.ifi.feedback.library.transaction;

import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public static Connection createDatabaseConnection() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "merovinger.1337");
    }
}

