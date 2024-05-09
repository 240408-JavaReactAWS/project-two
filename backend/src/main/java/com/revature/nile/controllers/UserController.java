package com.revature.nile.controllers;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.OrderItem;
import com.revature.nile.models.User;
import com.revature.nile.services.OrderService;
import com.revature.nile.services.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService us, OrderService os) {
        this.us = us;
        this.os = os;
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

    /**
     * UPDATE ORDER ITEM QUANTITY
     * */
    @PatchMapping("{userId}/orders/current")
    public ResponseEntity<String> updateOrderItemHandler(@PathVariable("userId") int userId, @RequestHeader(name="username") String username, @RequestBody OrderItem orderItem) {

        if (orderItem.getQuantity() < 0){ //Quantity less than 0: Return 400 (Bad Request) with information “Can’t have a quantity less than zero!” in the response body.
            return new ResponseEntity<>("Can’t have a quantity less than zero!", BAD_REQUEST);
        } else if (orderItem.getQuantity() > orderItem.getItem().getStock()){ // Quantity greater than Item.stock: Return 400 (Bad Request) with information "Requested quantity higher than current stock." in the response body.
            return new ResponseEntity<>("Requested quantity higher than current stock!", BAD_REQUEST);
        }
        User loggedInUser;
        try {
            loggedInUser = us.getUserById(userId);
            if(loggedInUser.getUserId() != userId){
                throw new EntityExistsException("User ID and logged-in user ID mismatch");
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(FORBIDDEN); // Logged-in user ID and path parameter ID mismatch: Return 403 (Forbidden)
        }
        OrderItem updatedOrderItem /** Update the quantity of the order item */
                = us.editCartItemQuantity(userId, orderItem.getItem().getItemId(), orderItem.getQuantity());
        if(updatedOrderItem == null){
            return new ResponseEntity<>(NOT_FOUND); /** Item not in the current order: Return 404 (Not Found) */
        }
        return new ResponseEntity<>("Quantity Updated", OK);
    }

}
