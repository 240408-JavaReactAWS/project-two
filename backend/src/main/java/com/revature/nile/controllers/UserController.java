package com.revature.nile.controllers;

import com.revature.nile.exceptions.ItemNotFoundExceptions;
import com.revature.nile.models.*;
import com.revature.nile.services.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import javax.naming.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.HttpStatus.*;
import java.util.ArrayList;

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
    public ResponseEntity<User> registerNewUserHandler(@RequestBody User newUser) {
        User registerUser;
        try {
            registerUser = us.registerUser(newUser);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(newUser, CREATED);
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
    public ResponseEntity<Order> addItemToOrderHandler(@PathVariable("id") int id, @RequestHeader(name="userId") int userId, @RequestBody OrderItem orderItem) {
        if (id != userId)
            return new ResponseEntity<>(FORBIDDEN);
        User user;
        // Get the user by ID
        try {
            user = us.getUserById(userId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        if (orderItem.getItem() == null || orderItem.getQuantity() <= 0)
            return new ResponseEntity<>(BAD_REQUEST);
        Item item;
        try {
            item = is.getItemById(orderItem.getItem().getItemId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (item.getStock() < orderItem.getQuantity())
            return new ResponseEntity<>(BAD_REQUEST);
        orderItem.setItem(item);
        //If the user does not have a current order, create a new order for them
        Order finalOrder;
        try {
            finalOrder = os.addOrderItemToCart(userId, orderItem);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (finalOrder == null)
            return new ResponseEntity<>(NOT_FOUND);

        return new ResponseEntity<Order>(os.getOrderById(finalOrder.getOrderId()), CREATED);
    }

    /**
     * UPDATE ORDER ITEM QUANTITY
     * */
    // @PatchMapping("{userId}/orders/current")
    // public ResponseEntity<String> updateOrderItemHandler(@PathVariable("userId") int userId, @RequestHeader(name="username") String username, @RequestBody OrderItem orderItem) {

    //     if (orderItem.getQuantity() < 0){ //Quantity less than 0: Return 400 (Bad Request) with information “Can’t have a quantity less than zero!” in the response body.
    //         return new ResponseEntity<>("Can’t have a quantity less than zero!", BAD_REQUEST);
    //     } else if (orderItem.getQuantity() > orderItem.getItem().getStock()){ // Quantity greater than Item.stock: Return 400 (Bad Request) with information "Requested quantity higher than current stock." in the response body.
    //         return new ResponseEntity<>("Requested quantity higher than current stock!", BAD_REQUEST);
    //     }
    //     User loggedInUser;
    //     try {
    //         loggedInUser = us.getUserById(userId);
    //         if(loggedInUser.getUserId() != userId){
    //             throw new EntityExistsException("User ID and logged-in user ID mismatch");
    //         }
    //     } catch (EntityNotFoundException e) {
    //         return new ResponseEntity<>(FORBIDDEN); // Logged-in user ID and path parameter ID mismatch: Return 403 (Forbidden)
    //     }
    //     OrderItem updatedOrderItem /** Update the quantity of the order item */
    //             = us.editCartItemQuantity(userId, orderItem.getItem().getItemId(), orderItem.getQuantity());
    //     if(updatedOrderItem == null){
    //         return new ResponseEntity<>(NOT_FOUND); /** Item not in the current order: Return 404 (Not Found) */
    //     }
    //     return new ResponseEntity<>("Quantity Updated", OK);
    // }

    // This function retrieves all items for a specific user
    @GetMapping("{userId}/items")
    public ResponseEntity<List<Item>> getItemsByUserIdHandler(@PathVariable int userId, @RequestHeader("userId") int userIdHeader) {
        User user;
        List<Item> items;
        try {
            if (userIdHeader != userId) {
                return new ResponseEntity<>(FORBIDDEN);
            }
            user = us.getUserById(userId);
            items = is.getItemsByUserId(user.getUserId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(FORBIDDEN);
        } catch (ItemNotFoundExceptions e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(items, OK);
    }
}
