package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class AccountManager {
    Map<String, User> accounts = new HashMap<String, User>();

    public void AccountManager(){
        this.accounts.put("Alice", new User("Alice","abc"));
        this.accounts.put("Bob", new User("Bob","bcd"));
        this.accounts.put("Charlie", new User("Charlie","cde"));
    }

    public void addAccount(User user){
        accounts.put(user.getUsername(), user);
    }

    public void printMap(){
        for(Map.Entry m:accounts.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
        }
    }

    public User login(String username, String password){
        return this.accounts.get(username)!=null ? this.accounts.get(username) : null;
    }

    public String setToken(User user){
        User currentUser = login(user.getUsername(), user.getPassword());
        System.out.println(currentUser);
        if(currentUser == null){
            user.setToken(String.valueOf(Objects.hash(user.getPassword(), user.getUsername())));
            this.addAccount(user);
        }
        return this.accounts.get(user.getUsername()).getToken();
    }

    public boolean authenticateUser(String token){
        for(Map.Entry<String, User> user:accounts.entrySet()){
            if(String.valueOf(Objects.hash(user.getValue().getPassword(), user.getValue().getUsername())).equals(token)){
                return true;
            }
        }
        return false;
    }


}
