package com.example.javaproject.Authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private int userID;
    private int password;
    private int token;

    public User(int userID, String password){
        this.userID = userID;
        this.password = Objects.hash(password);
    }
}
