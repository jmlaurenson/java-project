package com.example.javaproject.Authentication;

import com.example.javaproject.Authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private AccountManager accountManager;

    //tells Spring to automatically use its own dependency features
    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @PostMapping(value = "/logIn")
    ResponseEntity<User> addOrder(@RequestBody User user) {
        //createToken
        accountManager.addAccount(user);
        accountManager.printMap();
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
