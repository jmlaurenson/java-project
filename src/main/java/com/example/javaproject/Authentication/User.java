package com.example.javaproject.Authentication;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@EqualsAndHashCode
public class User {
    private int userID;
    private String password;
    private int token;

    public User(int userID, String password){
        this.userID = userID;
        this.password = bCryptPasswordEncoder().encode(password);//Objects.hash(password);
        System.out.println(password);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
