package com.example.javaproject.Authentication.JWT;

import com.example.javaproject.Authentication.User;
import org.springframework.stereotype.Repository;

public interface UserRepository extends Repository {
    User findByUsername(String username);
}