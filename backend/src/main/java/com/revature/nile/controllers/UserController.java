package com.revature.nile.controllers;

import com.revature.nile.exceptions.ItemNotFoundExceptions;
import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.services.ItemService;
import com.revature.nile.services.OrderService;
import com.revature.nile.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController  {
    private final UserService us;
    private final OrderService os;
    private final ItemService is;

    @Autowired
    public UserController(UserService us, OrderService os, ItemService is) {
        this.us = us;
        this.os = os;
        this.is = is;
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

    /*
     * This function adds an Item to the User's current order.
     * We check to make sure a current order exists by checking the user's order list.
     * If the user does not have a current order, we create a new order for them.
     * We then create a new order item and add it to the order.
     * Finally, we return the updated order.
     * 
     * Currently, we have to have the entire Item object in the orderItem.
     */
    @PostMapping("{id}/orders/current")
    public ResponseEntity<Order> addItemToOrderHandler(@PathVariable("id") int userId, @RequestBody OrderItem orderItem) {
        User user;
        System.out.println(orderItem.toString());
        // Get the user by ID
        try {
            user = us.getUserById(userId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        //If the user does not have a current order, create a new order for them
        Order currOrder = os.getCurrentOrderByUserId(userId);
        if (currOrder == null) {
            Order newOrder = new Order();
            newOrder.setUser(user);
            newOrder.setStatus(Order.StatusEnum.PENDING);
            newOrder.setBillAddress(user.getAddress());
            newOrder.setShipToAddress(user.getAddress());
            newOrder.setOrderItems(new ArrayList<OrderItem>());
            os.createOrder(newOrder);
            currOrder = newOrder;
        }
        orderItem.setOrder(currOrder);
        os.createOrderItem(orderItem);
        currOrder.getOrderItems().add(orderItem);

        return new ResponseEntity<Order>(os.getOrderById(currOrder.getOrderId()), CREATED);
    }

    // This function retrieves all items for a specific user
    @GetMapping("/{userId}/items")
    public ResponseEntity<List<Item>> getItemsByUserIdHandler(@PathVariable int userId, @RequestHeader("userId") String userIdHeader) {
        User user;
        List<Item> items;
        try {
            int userIdInt = Integer.parseInt(userIdHeader);
            if (userIdInt != userId) {
                return new ResponseEntity<>(FORBIDDEN);
            }
            user = us.getUserById(userId);
            items = is.getItemsByUserId(user.getUserId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(FORBIDDEN);
        }catch (ItemNotFoundExceptions e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(items, OK);
    }

    @PatchMapping("/{userId}/orders/current")
    public ResponseEntity<OrderItem> updateOrderStatusHandler(
            @PathVariable int userId,
            @RequestBody OrderItem orderItem,
            @RequestHeader("userId") int userIdHeader) {

        if (userIdHeader != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        us.removeItemFromPendingOrder(userId, orderItem.getItem().getItemId());
        return ResponseEntity.ok(orderItem);
    }


}
