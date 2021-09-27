package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AccountManager {
    Map<Integer, User> accounts = new HashMap<Integer, User>();

    public void AccountManager(){
        this.accounts.put(1, new User(1,"abc"));
        this.accounts.put(2, new User(2,"bcd"));
        this.accounts.put(3, new User(3,"cde"));
    }

    public void addAccount(User user){
        accounts.put(user.getUserID(), user);
    }

    public void printMap(){
        for(Map.Entry m:accounts.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
        }
    }

    public User login(int userID, String password){
        return this.accounts.get(userID)!=null ? this.accounts.get(userID) : null;
    }

    public String setToken(User user){
        User currentUser = login(user.getUserID(), user.getPassword());
        System.out.println(currentUser);
        if(currentUser == null){
            user.setToken(String.valueOf(Objects.hash(user.getPassword(), user.getUserID())));
            this.addAccount(user);
        }
        return this.accounts.get(user.getUserID()).getToken();
    }

    public boolean authenticateUser(String token){
        for(Map.Entry<Integer, User> user:accounts.entrySet()){
            if(String.valueOf(Objects.hash(user.getValue().getPassword(), user.getValue().getUserID())).equals(token)){
                return true;
            }
        }
        return false;
    }

    public boolean authenticateUserByID(String token, int userID){
        System.out.println(accounts.get(userID));
        if(accounts.get(userID)!=null){
            if(String.valueOf(Objects.hash(accounts.get(userID).getPassword(), accounts.get(userID).getUserID())).equals(token)){
                return true;
            }
        }


        return false;
    }


}
