package com.example.javaproject.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class AccountManager {
    Map<Integer, User> accounts = new HashMap<Integer, User>();
    DBManager dbManager;

    //tells Spring to automatically use its own dependency features
    @Autowired
    public  AccountManager(DBManager dbManager){
            this.dbManager = dbManager;

    }

    //Checks if the userID is present in the map
    public Optional<User> getUser(int userID){
        return this.accounts.get(userID)!=null ? Optional.of(this.accounts.get(userID)) : Optional.empty();
    }

    //If the user does not exist, generate and return a new token and add the user
    public String setToken(User user){
        Optional<User> currentUser = getUser(user.getUserID());
        // If user is not present, create a token and add the user to the map
        if(currentUser.isEmpty()){
            user.setToken(createToken(user));
            accounts.put(user.getUserID(), user);
            dbManager.addTableToDB(); //Creates a table in the database if none is present
            dbManager.addUserToDB(user); //Add the current user to the database

        }
        //dbManager.printDB();
        return accounts.get(user.getUserID()).getToken();
    }

    public String createToken(User user) {
        return String.valueOf(Objects.hash(user.getPassword(), user.getUserID()));
    }

    //Loop through every user for the current token and return true if authenticated
    public boolean authenticateUser(String token){
        return accounts.entrySet()
                .stream()
                .anyMatch(v -> createToken(v.getValue())
                        .equals(token));
    }

    //Check the map for a user ID and check if its token matches the token passed in
    public boolean authenticateUserByID(String token, int userID){
        if(accounts.get(userID)!=null){
            return createToken(accounts.get(userID)).equals(token);
        }
        return false;
    }
}
