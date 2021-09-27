package com.example.javaproject.Authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private int userID;
    private String password;
    private String token;

    public User(int userID, String password){
        this.userID = userID;
        this.password = password;
    }
}
