package com.revature.nile.controllers;

import com.revature.nile.models.User;
import com.revature.nile.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController  {
    private final UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }

    @PostMapping("register")
    public ResponseEntity<User> registerNewUserHandler(@RequestBody User newUser){
        User registerUser;
        try{
            registerUser = us.registerUser(newUser);
        } catch (EntityExistsException e){
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(registerUser, CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<User> loginHandler(@RequestBody User loginAttempt) {
        User user;
        try {
            user = us.loginUser(loginAttempt);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("logout")
    public ResponseEntity<User> logoutHandler(@RequestBody User logoutAttempt) {
        try {
            us.logoutUser(logoutAttempt);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<User> getUserByIdHandler(@PathVariable int userId) {
        User user;
        try {
            user = us.getUserById(userId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsersHandler() {
        return ResponseEntity.ok(us.getAllUsers());
    }
}
