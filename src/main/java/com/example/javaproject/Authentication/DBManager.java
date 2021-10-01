package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Optional;
import java.util.Properties;

@Component
public class DBManager {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test4";



    public void addTableToDB() {
        Optional<Connection> conn;
            conn = getConnection();//DriverManager.getConnection(DB_URL,properties);
            if(conn.isPresent()){
                String sql = " CREATE TABLE IF NOT EXISTS ACCOUNT\n" +
                        " (userID      INTEGER NOT NULL PRIMARY KEY,\n" +
                        " password    INTEGER NOT NULL,\n" +
                        " token    INTEGER NOT NULL);\n";

                executeStatement(conn.get(), sql);
            }
    }

    public void addUserToDB(User user) {
        Optional<Connection> conn;
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
                System.out.println("SQL ERROR");
            } // nothing we can do
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    //Connects to the database using the correct credentials and returns the connection
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

    //Searches the database for a user with a specific token/ID and returns true if it is found
    public boolean getUser(String sql) {
        boolean result = false;
        Statement stmt = null;
        Optional<Connection> conn = getConnection();
        try{
            stmt = conn.get().createStatement();
            result = stmt.executeQuery(sql).next();
            stmt.close();
            conn.get().close();
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
                if(conn!=null) conn.get().close();
            } catch(SQLException se){
                System.out.println("Error 4");
                se.printStackTrace();
            }
        }
        return result;
    }

    //Searches for a user with an ID and returns a User object
    public Optional<User> checkUserExists(int userID) {
        Statement stmt = null;
        ResultSet resultSet = null;
        Optional<Connection> conn = getConnection();
        try{
            String sql =  "SELECT * FROM ACCOUNT WHERE token="+userID+";";
            stmt = conn.get().createStatement();
            resultSet = stmt.executeQuery(sql);
            //stmt.close();

        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        try {
            if(resultSet.next()){
                //Create and return the user found
                User user = new User(Integer.parseInt(resultSet.getString(0)), "");
                //As the constructor hashes the password in the constructor, the password must be set directly
                user.setPassword(resultSet.getString(1));
                user.setToken(Integer.parseInt(resultSet.getString(2)));
                conn.get().close();
                stmt.close();
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}