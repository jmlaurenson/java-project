package com.example.javaproject.Authentication;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccountManager {
    Map<String, String> accounts = new HashMap<String, String>();

    public void addAccount(User user){
        accounts.put(user.username, user.password);
    }

    public void printMap(){
        for(Map.Entry m:accounts.entrySet()){
            System.out.println(m.getKey()+" "+m.getValue());
        }
    }

}
