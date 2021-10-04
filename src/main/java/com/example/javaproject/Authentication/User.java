package com.example.javaproject.Authentication;
import java.io.Serializable;
import java.util.Objects;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
    private int userID;
    private String password;
    private int token;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User(int userID, String password){
        this.userID = userID;
        this.password = bCryptPasswordEncoder.encode(password);//Objects.hash(password);
        System.out.println(password);
    }


}
