package com.revature.nile.controllers;

import com.revature.nile.models.User;
import com.revature.nile.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.security.auth.login.FailedLoginException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
public class UserController  {
    private UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerNewUserHandler(@RequestBody User credentials, HttpSession session){
        User newUser;
        try{
            newUser = us.registerUser(credentials);
            session.setAttribute("user", newUser); // Store the user in the session
        } catch (EntityExistsException e){
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(newUser, CREATED);
    }

    public ResponseEntity<User> loginHandler(@RequestBody User loginAttempt, HttpSession session) {
        User user;
        try {
            user = us.loginUser(loginAttempt);
            session.setAttribute("user", user); // Store the user in the session
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, OK);
    }
}
