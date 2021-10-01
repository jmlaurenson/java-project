package com.example.javaproject.Authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountManagerTest {

    private  AccountManager accountManager;
    private DBManager dbManager;
    @BeforeEach
    public void setUp(){
        dbManager = mock(DBManager.class);
        accountManager =new AccountManager(dbManager);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Test
    @DisplayName("Check that a new account can be added and assigned a token")
    void addNewAccountAndReceiveToken() {
        //Arrange
        var user = new User(5, "Password");
        var token = accountManager.setToken(user);
        user.setToken(token);
        when(dbManager.checkUserExists(anyInt())).thenReturn(Optional.of(user));

        //Act
        Optional<User> result = accountManager.getUser(5);
        int predictedToken = Objects.hash(accountManager.getUser(5).get().getPassword(),5);
        //Assert
        assertAll(
                () -> assertTrue(bCryptPasswordEncoder().matches("Password", result.get().getPassword()), "PASSWORD IS INCORRECT"),
                () -> assertEquals(predictedToken, result.get().getToken(), "TOKEN IS INCORRECT")
        );
    }

    @Test
    @DisplayName("Check that two users can be added to the same map")
    void twoUsersAdded() {
        //Arrange
        var user1 = new User(1, "Password");
        var token = accountManager.setToken(user1);
        user1.setToken(token);
        var user2 = new User(2, "Password2");
        token = accountManager.setToken(user2);
        user2.setToken(token);
        when(dbManager.checkUserExists(1)).thenReturn(Optional.of(user1));
        when(dbManager.checkUserExists(2)).thenReturn(Optional.of(user2));


        //Act
        Optional<User> result1 = accountManager.getUser(1);
        Optional<User> result2 = accountManager.getUser(2);
        int predictedToken1 = Objects.hash(accountManager.getUser(1).get().getPassword(),1);
        int predictedToken2 = Objects.hash(accountManager.getUser(2).get().getPassword(),2);

        //Assert
        assertAll(
                () -> assertTrue(bCryptPasswordEncoder().matches("Password",bCryptPasswordEncoder().encode("Password"))),
                () -> assertTrue(bCryptPasswordEncoder().matches("Password",result1.get().getPassword()), "PASSWORD IS INCORRECT"),
                () -> assertEquals(predictedToken1, result1.get().getToken(), "TOKEN IS INCORRECT"),
                () -> assertTrue(bCryptPasswordEncoder().matches("Password2",result2.get().getPassword()), "PASSWORD IS INCORRECT"),
                () -> assertEquals(predictedToken2, result2.get().getToken(), "TOKEN IS INCORRECT")
        );
    }
    @Test
    @DisplayName("Authenticate user successfully")
    void authenticateUser() {
        //Arrange
        var user = new User(1, "Password");
        var token = accountManager.setToken(user);
        user.setToken(token);
        when(dbManager.checkUserExists(1)).thenReturn(Optional.of(user));
        when(dbManager.getUser("SELECT * FROM ACCOUNT WHERE token="+token+";")).thenReturn(true);

        //Act
        boolean result = accountManager.authenticateUser(accountManager.getUser(1).get().getToken());
        //Assert
        assertTrue(result, "USER NOT AUTHENTICATED");
    }

    @Test
    @DisplayName("Find no match in authentication")
    void findNoAuthentication() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUser(123);
        //Assert
        assertFalse(result, "INCORRECT USER AUTHENTICATED");
    }

    @Test
    @DisplayName("Authenticate user successfully by ID")
    void authenticateUserByID() {
        //Arrange
        var user = new User(1, "Password");
        var token = accountManager.setToken(user);
        user.setToken(token);
        when(dbManager.checkUserExists(1)).thenReturn(Optional.of(user));
        when(dbManager.getUser("SELECT * FROM ACCOUNT WHERE token="+token+" AND userID="+user.getUserID()+";")).thenReturn(true);
        //Act
        boolean result = accountManager.authenticateUserByID(accountManager.getUser(1).get().getToken(), 1);
        //Assert
        assertTrue(result, "USER NOT FOUND");
    }

    @Test
    @DisplayName("Find no match in authentication when the ID is correct")
    void findNoAuthenticationByID() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUserByID(123, 1);
        //Assert
        assertFalse(result, "INCORRECT USER AUTHENTICATED");
    }

    @Test
    @DisplayName("Find no match in authentication when the token is correct")
    void findNoAuthenticationByToken() {
        //Arrange
        var user = new User(1, "Password");
        var token = accountManager.setToken(user);
        user.setToken(token);
        when(dbManager.checkUserExists(1)).thenReturn(Optional.of(user));
        when(dbManager.getUser("SELECT * FROM ACCOUNT WHERE token="+token+" AND userID="+user.getUserID()+";")).thenReturn(true);
        //Act
        boolean result = accountManager.authenticateUserByID(accountManager.getUser(1).get().getToken(), 2);
        //Assert
        assertFalse(result, "INCORRECT USER AUTHENTICATED");
    }
}