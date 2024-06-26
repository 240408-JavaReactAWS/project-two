package com.revature.nile.services;

import com.revature.nile.exceptions.OrderProcessingException;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
        //Hashing the password
        try {
            byte[] salt = new byte[16];
            SecureRandom.getInstanceStrong().nextBytes(salt);
            user.setSalt(salt);
            user.setPassword(getHashedPassword(user.getPassword(), salt));
        } catch (Exception e) {
            throw new UserAlreadyExistsException("Error hashing password");
        }
        return ur.save(user);
    }

    public User loginUser(User user) throws AuthenticationException, EntityNotFoundException {
        Optional<User> optionalUser = ur.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User u = optionalUser.get();
            try {
                if (validatePassword(user.getPassword(), u)) {
                    return u;
                }
            } catch (NoSuchAlgorithmException e) {
                throw new AuthenticationException("Error validating password");
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

    public List<Order> viewOrderHistory(int userId) throws EntityNotFoundException {
        Optional<User> user = ur.findById(userId);
        if (user.isPresent()) {
            return user.get().getOrders();
        }
        throw new EntityNotFoundException("User with id: " + userId + " doesn't exist");
    }

    @Transactional
    public OrderItem removeItemFromPendingOrder(int userId, int itemId, int itemQuantity) throws OrderProcessingException {
        Optional<OrderItem> orderItemOpt = orderItemRepository.findByItemItemIdAndOrderUserUserId(itemId, userId);
        //check if the order item exists
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            String orderStatus = String.valueOf(orderItem.getOrder().getStatus());
            if (itemQuantity == 0 && "PENDING".equals(orderStatus)) {
                //if both condition are true, delete the order item
                orderItemRepository.deleteByItemItemId(itemId);
            } else {
                //if either condition is false, update the quantity of the order item
                orderItem.setQuantity(itemQuantity);
                orderItemRepository.save(orderItem);
            }
            return orderItemOpt.get();
        } else {
            //if the order item does not exist, throw an exception
            throw new OrderProcessingException("Order item not found for userId: " + userId + " and itemId: " + itemId);
        }
    }

    /*
     * Validates a user's password
     * @param password the password to validate
     * @param user the user to validate the password for
     * @return true if the password is correct, false otherwise
     */
    private static Boolean validatePassword(String password, User user) throws NoSuchAlgorithmException{
        byte[] salt = user.getSalt();
        byte[] passwordBytes = password.getBytes();
        byte[] saltedPassword = new byte[salt.length + passwordBytes.length];
        System.arraycopy(passwordBytes, 0, saltedPassword, 0, passwordBytes.length);
        System.arraycopy(salt, 0, saltedPassword, passwordBytes.length, salt.length);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(saltedPassword);
        return user.getPassword().equals(bytesToHex(hash));
    }

    private static String getHashedPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        byte[] passwordBytes = password.getBytes();
        byte[] saltedPassword = new byte[salt.length + passwordBytes.length];
        System.arraycopy(passwordBytes, 0, saltedPassword, 0, passwordBytes.length);
        System.arraycopy(salt, 0, saltedPassword, passwordBytes.length, salt.length);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(saltedPassword);
        return bytesToHex(hash);
    }

    /*
     * Converts a byte array to a hex string
     * @param hash the byte array to convert
     * @return the hex string
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}