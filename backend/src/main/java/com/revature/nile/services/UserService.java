package com.revature.nile.services;

import com.revature.nile.exceptions.OrderProcessingException;
import com.revature.nile.models.Item;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    private final UserRepository ur;

    private final OrderItemRepository or;

    @Autowired
    public UserService(UserRepository ur, OrderItemRepository or) {
        this.ur = ur;
        this.or = or;
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

    //remove an item from the pending order if the quantity from the order item is 0 and the order status is pending
    @Transactional
    public OrderItem removeItemFromPendingOrder(int userId, int itemId, int itemQuantity) throws OrderProcessingException {
        Optional<OrderItem> orderItemOpt = or.findByItemItemIdAndOrderUserUserId(itemId, userId);
        //check if the order item exists
        if (orderItemOpt.isPresent()) {
            OrderItem orderItem = orderItemOpt.get();
            String orderStatus = String.valueOf(orderItem.getOrder().getStatus());
            if (itemQuantity == 0 && "PENDING".equals(orderStatus)) {
                //if both condition are true, delete the order item
                or.deleteByItemItemId(itemId);
            } else {
                //if either condition is false, update the quantity of the order item
                orderItem.setQuantity(itemQuantity);
                or.save(orderItem);
            }
            return orderItemOpt.get();
        } else {
            //if the order item does not exist, throw an exception
            throw new OrderProcessingException("Order item not found for userId: " + userId + " and itemId: " + itemId);
        }
    }


}