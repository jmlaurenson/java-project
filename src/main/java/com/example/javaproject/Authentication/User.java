package com.example.javaproject.Authentication;
import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
    @JsonProperty("userID")
    private int userID;

    @JsonProperty("password")
    private String password;

    @JsonProperty("token")
    private int token;

    @JsonProperty("bCryptPasswordEncoder")
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User(){

    }

    public User(int userID, String password){
        this.userID = userID;
        this.password = bCryptPasswordEncoder.encode(password);//Objects.hash(password);
        System.out.println(password);
    }


}
