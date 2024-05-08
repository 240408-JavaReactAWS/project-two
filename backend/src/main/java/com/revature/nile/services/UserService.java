package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.User;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository ur;

    @Autowired
    public UserService(UserRepository ur) {
        this.ur = ur;
    }

    public User registerUser(User user) throws EntityExistsException {
        Optional<User> optionalUser = ur.findByUserName(user.getUserName());
        if (optionalUser.isPresent()) {
            throw new EntityExistsException(user.getUserName() + " already exists");
        }
        return ur.save(user);
    }

    public User loginUser(User user) throws AuthenticationException, EntityNotFoundException {
        Optional<User> optionalUser = ur.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            if(u.getPassword().equals(user.getPassword())) {
                return u;
            }
            throw new AuthenticationException("Incorrect Password");
        }
        throw new EntityNotFoundException(user.getEmail() + " doesn't exist");
    }

    public void logoutUser(User logoutAttempt) {
        Optional<User> optionalUser = ur.findByEmail(logoutAttempt.getEmail());
        if (optionalUser.isPresent()) {
            return;
        }
        throw new EntityNotFoundException(logoutAttempt.getEmail() + " doesn't exist");
    }

    public User getUserById(int userId) throws EntityNotFoundException {
        Optional<User> user = ur.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new EntityNotFoundException("User with id: " + userId + " doesn't exist");
    }

    public List<User> getAllUsers() {
        return ur.findAll();
    }

    public User updateUser(User user) {
        return ur.save(user);
    }

}