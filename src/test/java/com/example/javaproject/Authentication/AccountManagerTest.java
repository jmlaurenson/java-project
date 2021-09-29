package com.example.javaproject.Authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountManagerTest {
    private AccountManager accountManager;


    @Test
    @DisplayName("Check that a new account can be added and assigned a token")
    void addNewAccountAndReceiveToken() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));

        //Act
        User result = accountManager.accounts.get(1);
        String predictedToken = String.valueOf(Objects.hash(Objects.hash("Password"), 1));

        //Assert
        assertAll(
                () -> assertEquals(Objects.hash("Password"), result.getPassword(), "PASSWORD IS INCORRECT"),
                () -> assertEquals(predictedToken, result.getToken(), "TOKEN IS INCORRECT")
        );
    }

    @Test
    @DisplayName("Check that two users can be added to the same map")
    void twoUsersAdded() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        accountManager.setToken(new User(2, "Password2"));

        //Act
        User result1 = accountManager.accounts.get(1);
        User result2 = accountManager.accounts.get(2);
        String predictedToken1 = String.valueOf(Objects.hash(Objects.hash("Password"), 1));
        String predictedToken2 = String.valueOf(Objects.hash(Objects.hash("Password2"), 2));

        //Assert
        assertAll(
                () -> assertEquals(Objects.hash("Password"), result1.getPassword(), "PASSWORD IS INCORRECT"),
                () -> assertEquals(predictedToken1, result1.getToken(), "TOKEN IS INCORRECT"),
                () -> assertEquals(Objects.hash("Password2"), result2.getPassword(), "PASSWORD IS INCORRECT"),
                () -> assertEquals(predictedToken2, result2.getToken(), "TOKEN IS INCORRECT")
        );
    }
    @Test
    @DisplayName("Authenticate user successfully")
    void authenticateUser() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUser(accountManager.accounts.get(1).getToken());
        //Assert
        assertTrue(result, "USER NOT AUTHENTICATED");
    }

    @Test
    @DisplayName("Find no match in authentication")
    void findNoAuthentication() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUser("123");
        //Assert
        assertFalse(result, "INCORRECT USER AUTHENTICATED");
    }

    @Test
    @DisplayName("Authenticate user successfully by ID")
    void authenticateUserByID() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUserByID(accountManager.accounts.get(1).getToken(), 1);
        //Assert
        assertTrue(result, "USER NOT FOUND");
    }

    @Test
    @DisplayName("Find no match in authentication when the ID is correct")
    void findNoAuthenticationByID() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUserByID("123", 1);
        //Assert
        assertFalse(result, "INCORRECT USER AUTHENTICATED");
    }

    @Test
    @DisplayName("Find no match in authentication when the token is correct")
    void findNoAuthenticationByToken() {
        //Arrange
        accountManager.setToken(new User(1, "Password"));
        //Act
        boolean result = accountManager.authenticateUserByID(accountManager.accounts.get(1).getToken(), 2);
        //Assert
        assertFalse(result, "INCORRECT USER AUTHENTICATED");
    }
}