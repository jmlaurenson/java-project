package com.example.javaproject.Authentication;

import com.example.javaproject.Authentication.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class AuthenticationController {
    private AccountManager accountManager;

    //tells Spring to automatically use its own dependency features
    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @PostMapping(value = "/logIn")
    ResponseEntity<User> login(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).header("token", accountManager.setToken(user)).body(user);
    }

    @GetMapping("/authenticate/{token}")
    ResponseEntity<String> authenticateUser(@PathVariable String token) {
        //accountManager.addAccount(user);
        //accountManager.printMap();
        return accountManager.authenticateUser(token) ? ResponseEntity.status(HttpStatus.FOUND).body("User authenticated") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not authenticated");
    }
}
