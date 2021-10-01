package com.example.javaproject.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class AccountManager {
    //Map<Integer, User> accounts = new HashMap<Integer, User>();
    DBManager dbManager;

    //tells Spring to automatically use its own dependency features
    @Autowired
    public  AccountManager(DBManager dbManager){
            this.dbManager = dbManager;

    }

    //Checks if the userID is present in the map
    public Optional<User> getUser(int userID){
        dbManager.addTableToDB(); //Creates a table in the database if none is present
        return dbManager.checkUserExists(userID);
        //return this.accounts.get(userID)!=null ? Optional.of(this.accounts.get(userID)) : Optional.empty();
    }

    //If the user does not exist, generate and return a new token and add the user
    public int setToken(User user){
        Optional<User> currentUser = getUser(user.getUserID());
        // If user is not present, create a token and add the user to the map
        if(currentUser.isEmpty()){
            user.setToken(createToken(user));
            dbManager.addTableToDB(); //Creates a table in the database if none is present
            dbManager.addUserToDB(user); //Add the current user to the database
        }
        return user.getToken();
    }

    public int createToken(User user) {
        return Objects.hash(user.getPassword(), user.getUserID());
    }

    //Check the database for a user with a matching token and return true if authenticated
    public boolean authenticateUser(int token){
        return dbManager.getUser("SELECT * FROM ACCOUNT WHERE token="+token+";");
    }

    //Check the database for a user with a matching ID and token
    public boolean authenticateUserByID(int token, int userID){
        return dbManager.getUser("SELECT * FROM ACCOUNT WHERE token="+token+" AND userID="+userID+";");
    }
}
