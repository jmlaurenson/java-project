package com.example.javaproject.Authentication;
import java.util.Objects;
import lombok.*;
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


    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
