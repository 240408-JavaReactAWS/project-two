package com.revature.nile.services;

import com.revature.nile.exceptions.UserAlreadyExistsException;
import com.revature.nile.models.Item;
import com.revature.nile.models.User;
import com.revature.nile.repositories.UserRepository;
import com.revature.nile.models.Order;
import com.revature.nile.repositories.OrderRepository;
import com.revature.nile.models.OrderItem;
import com.revature.nile.repositories.OrderItemRepository;

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
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public UserService(UserRepository ur, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.ur = ur;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public User registerUser(User user) throws EntityExistsException {
        //Checking that a user with the same Username does not already exist; throw an exception if one does
        Optional<User> optionalUser = ur.findByUserName(user.getUserName());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(user.getUserName() + " already exists");
        }
        //Checking that a user with the same email does not already exist; throw an exception if one does
        Optional<User> userWithEmail = ur.findByEmail(user.getEmail());
        if (userWithEmail.isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
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

    /**
     * EDIT CART ITEM QUANTITY
     * */
    // public OrderItem editCartItemQuantity(int userId, int itemId, int quantity){
    //     Optional<Order> orderOptional = orderRepository.findByUserIdAndStatus(userId, "PENDING");
    //     if (orderOptional.isPresent()) {
    //         Order order = orderOptional.get();
    //         OrderItem orderItem = orderItemRepository.findByItemIdAndOrder(itemId, order);
    //         orderItem.setQuantity(quantity);
    //         return orderItemRepository.save(orderItem);
    //     }
    //     return null;
    // }
}