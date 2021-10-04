package com.example.javaproject.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private AccountManager accountManager;

    //tells Spring to automatically use its own dependency features
    @Autowired
    AuthenticationController(AccountManager accountManager){

        this.accountManager = accountManager;
    }


    @PostMapping(value = "/login2")
    ResponseEntity<User> login(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).header("token", Integer.toString(accountManager.setToken(user))).body(user);
    }
}