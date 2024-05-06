package com.revature.nile.services;

import com.revature.nile.models.User;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository ur;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository ur, PasswordEncoder passwordEncoder) {
        this.ur = ur;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        Optional<User> optionalUser = ur.findByUsername(user.getUsername());
        if(optionalUser.isPresent()) {
            throw new EntityExistsException(user.getUsername()+" already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return ur.save(user);
    }

    public User loginUser(User user) throws AuthenticationException {
        Optional<User> optionalUser = ur.findByEmail(user.getEmail());
        if(optionalUser.isPresent()) {
            User u = optionalUser.get();
            if(u.getPassword().equals(user.getPassword())) {
                return u;
            }
            throw new AuthenticationException("Incorrect Password");
        }
        throw new EntityNotFoundException(user.getEmail()+" doesn't exist");
    }
}