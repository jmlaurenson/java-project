package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class AccountManager {
    Map<Integer, User> accounts = new HashMap<Integer, User>();

    //Create temp dummy accounts
    public void AccountManager(){
        this.accounts.put(1, new User(1,"abc"));
        this.accounts.put(2, new User(2,"bcd"));
        this.accounts.put(3, new User(3,"cde"));
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
            user.setToken(String.valueOf(Objects.hash(user.getPassword(), user.getUserID())));
            accounts.put(user.getUserID(), user);
        }
        return this.accounts.get(user.getUserID()).getToken();
    }

    //Loop through every user for the current token and return true if authenticated
    public boolean authenticateUser(String token){
        accounts.forEach((k, v) -> {
            if(String.valueOf(Objects.hash(v.getPassword(), v.getUserID())).equals(token)){
                return;
            }
        });
        return false;
    }

    //Check the map for a user ID and check if its token matches the token passed in
    public boolean authenticateUserByID(String token, int userID){
        if(accounts.get(userID)!=null){
            if(String.valueOf(Objects.hash(accounts.get(userID).getPassword(), accounts.get(userID).getUserID())).equals(token)){
                System.out.println("TRUE");
                return true;
            }
        }
        System.out.println("FALSE");
        return false;
    }
}
