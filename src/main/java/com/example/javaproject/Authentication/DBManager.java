package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Optional;
import java.util.Properties;

@Component
public class DBManager {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test3";



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

    public void print(ResultSet resultSet){
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = 0;

            columnsNumber = rsmd.getColumnCount();

        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + rsmd.getColumnName(i));
            }
            System.out.println("");
        }
        } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public ResultSet executeStatementSelect(Connection conn, String sql){
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            print(result);
            stmt.close();
            return result;

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
        return null;
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

    public boolean getUserByID(int token, int userID){
        Optional<Connection> conn;
        conn = getConnection();//DriverManager.getConnection(DB_URL,properties);
        if(conn.isPresent()){
            String sql =  "SELECT * FROM ACCOUNT WHERE token="+token+" AND userID="+userID+";";
            try{
                //Return true if a user is found
                conn.get().close();
                return executeStatementSelect(conn.get(), sql).next() ?true : false;
            } catch(SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            }
        }
        return false;


    }

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
            User user = new User(Integer.parseInt(resultSet.getString(0)), "");
            user.setPassword(Integer.parseInt(resultSet.getString(1)));
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