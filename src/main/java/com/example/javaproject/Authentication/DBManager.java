package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Component
public class DBManager {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";

    public void connectToDB() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            Properties properties = new Properties(); // Create Properties object
            properties.put("user", "sa");         // Set user ID for connection
            properties.put("password", "");     // Set password for connection
            conn = DriverManager.getConnection(DB_URL,properties);

            //STEP 3: Execute a query
            System.out.println("Creating table in given database...");

            String sql =  "INSERT INTO Account (userID, password, token) " +
                    "VALUES (123, 'password1', 123);";
            String sql1 = " CREATE TABLE IF NOT EXISTS TEAMS\n" +
                    " (userID      INTEGER NOT NULL PRIMARY KEY,\n" +
                    " password    VARCHAR(30) NOT NULL,\n" +
                    " token    INTEGER NOT NULL);\n";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql1);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            System.out.println("Error 1");
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            System.out.println("Error 2");
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
                System.out.println("Error 3");
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                System.out.println("Error 4");
                se.printStackTrace();
            } //end finally try
        } //end try
    }

}