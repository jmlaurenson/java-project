package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;

@Component
public class DBManager {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/te";

    public DBManager(){

    }

    public void addTableToDB() {
        Optional<Connection> conn = null;
            conn = getConnection();//DriverManager.getConnection(DB_URL,properties);
            if(conn.isPresent()){
                String sql = " CREATE TABLE IF NOT EXISTS ACCOUNT\n" +
                        " (userID      INTEGER NOT NULL PRIMARY KEY,\n" +
                        " password    VARCHAR(30) NOT NULL,\n" +
                        " token    INTEGER NOT NULL);\n";

                executeStatement(conn.get(), sql);
            }
    }

    public void addUserToDB(User user) {
        Optional<Connection> conn = null;
        conn = getConnection();//DriverManager.getConnection(DB_URL,properties);
        if(conn.isPresent()){
            String sql =  "INSERT INTO Account (userID, password, token) " +
                    "VALUES ("+user.getUserID()+", '"+user.getPassword()+"', "+user.getToken()+");";

            executeStatement(conn.get(), sql);
        }
    }

    public void executeStatement(Connection conn, String sql){
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
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
            }
        }

    }

    public Optional<Connection> getConnection(){
        try {
            Class.forName(JDBC_DRIVER);
            Properties properties = new Properties(); // Create Properties object
            properties.put("user", "sa");         // Set user ID for connection
            properties.put("password", "");     // Set password for connection
            return Optional.of(DriverManager.getConnection(DB_URL,properties));
        }
        catch(SQLException | ClassNotFoundException se) {
            //Handle errors for JDBC
            se.printStackTrace();
            return Optional.empty();
        }
    }


}