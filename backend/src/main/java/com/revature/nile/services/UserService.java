package com.revature.nile.services;

import com.revature.nile.models.User;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.OrderRepository;
import com.revature.nile.repositories.UserRepository;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
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

    public OrderItem editCartItemQuantity(int userId, int itemId, int quantity){
        Optional<Order> orderOptional = orderRepository.findByUserIdAndStatus(userId, "PENDING");
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            OrderItem orderItem = orderItemRepository.findByItemIdAndOrder(itemId, order);
            orderItem.setQuantity(quantity);
            return orderItemRepository.save(orderItem);
        }
        return null;
    }

}