package com.revature.nile.controllers;

import com.revature.nile.exceptions.ItemNotFoundExceptions;
import com.revature.nile.exceptions.NullAddressException;
import com.revature.nile.exceptions.UserNotFoundException;
import com.revature.nile.exceptions.OrderProcessingException;
import com.revature.nile.exceptions.UserAlreadyExistsException;
import com.revature.nile.models.*;
import com.revature.nile.services.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

import java.util.ArrayList;

@RestController
@RequestMapping("users")
//TO-DO: Update Cross-Origin to our S3 bucket before deploying!
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
public class UserController  {

    private final UserService us;
    private final OrderService os;
    private final ItemService is;
    private final EmailService es;

    @Autowired
    public UserController(UserService us, OrderService os, ItemService is, EmailService es) {
        this.us = us;
        this.os = os;
        this.is = is;
        this.es = es;
    }

    /*
     * Story ID 1: User registers for an account.
     * This function creates a new User and stores it in the database.
     * The function takes in the User object.
     * The function handles an EntityExistsException from the UserService by returning a BAD_REQUEST status.
     * The function handles a UserAlreadyExistsException from the UserService by returning a CONFLICT status.
     * On success, the function returns a CREATED status and the created User.
     */
    @PostMapping
    public ResponseEntity<User> registerNewUserHandler(@RequestBody User newUser) {
        User registerUser;
        try {
            registerUser = us.registerUser(newUser);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
            catch(UserAlreadyExistsException e) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        return new ResponseEntity<>(newUser, CREATED);
    }

    /*
     * Story ID 2: User logs in using email and password
     * This function logs in a User and returns the User object.
     * The function takes in the User object.
     * The function handles an AuthenticationException from the UserService by returning an UNAUTHORIZED status.
     * On success, the function returns an OK status and the User object.
     */
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

    /*
     * This function logs out a User.
     * The function takes in the User object.
     * The function handles an EntityNotFoundException from the UserService by returning a NOT_FOUND status.
     * On success, the function returns an OK status.
     * TO-DO: This function is currently not used to satisfy any user stories. Consider removing it.
     */
    @GetMapping("logout")
    public ResponseEntity<User> logoutHandler(@RequestBody User logoutAttempt) {
        try {
            us.logoutUser(logoutAttempt);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(OK);
    }

    /*
     * 
     */
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
     * The front end will pass in a partially-completed OrderItem object. It should have the ItemID and Quantity.
     * {
     *    "itemId": 1,
     *    "stock": 2
     * }
     * 
     * The other fields will be populated when the OrderItem is added to the database.
     */
    @PostMapping("{id}/orders/current")
/*<<<<<<< Cart-Checkout
    public ResponseEntity<Order> addItemToOrderHandler(@PathVariable("id") int id, @RequestHeader(name = "userId") int userId, @RequestBody OrderItem orderItem) {
=======*/
    public ResponseEntity<Order> addItemToOrderHandler(@PathVariable("id") int id, @RequestHeader(name="userId") int userId, 
        @RequestBody Item itemToItemOrderize) {
            
        //Make sure the Item we're trying to put in the cart exists!
        Item theRealItem;
        try{
            theRealItem = is.getItemById(itemToItemOrderize.getItemId());
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(NOT_FOUND);
        }
        //You can't put an item in a cart if the cart isn't yours!
/*>>>>>>> main*/
        if (id != userId)
            return new ResponseEntity<>(FORBIDDEN);
        User user;
        // Get the user by ID. If the user does not exist, return a 404 (Not Found) status.
        try {
            user = us.getUserById(userId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        //Create and populate a new OrderItem object to be added to the current order
        OrderItem orderItem = new OrderItem();
        //Set the relationship between the OrderItem (what we're putting in the cart) and the Item (that someone is selling)
        orderItem.setItem(theRealItem);
        orderItem.setQuantity(itemToItemOrderize.getStock());

        //if (itemToItemOrderize.getStock() <= 0)
        //    return new ResponseEntity<>(BAD_REQUEST);

        //You can't put less than 1 item in a cart!
        if(orderItem.getQuantity() <= 0)
            return new ResponseEntity<>(BAD_REQUEST);

        /*try {
            item = is.getItemById(orderItem.getItem().getItemId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }*/
        //You can't put more OrderItems in your cart than there are Items in stock!
        if (theRealItem.getStock() < orderItem.getQuantity())
            return new ResponseEntity<>(BAD_REQUEST);

        //If the user does not have a current order, create a new order for them
        Order currentOrder;
        try {
            currentOrder = os.addOrderItemToCart(userId, orderItem);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        if (currentOrder == null)
            return new ResponseEntity<>(NOT_FOUND);

        return new ResponseEntity<Order>(os.getOrderById(currentOrder.getOrderId()), CREATED);
    }


    @GetMapping("{userId}/orders/current")
    public ResponseEntity<Order> viewCurrentOrdersHandler(@PathVariable int targetUserId, @RequestHeader(name="userId") int userId) {
        User loggedInUser;
        try {
            loggedInUser = us.getUserById(userId);
            if(loggedInUser.getUserId() != targetUserId){
                    throw new EntityExistsException("User ID and logged-in user ID mismatch");
                }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(FORBIDDEN); // Logged-in user ID and path parameter ID mismatch: Return 403 (Forbidden)
        }

        return new ResponseEntity<>(os.getCurrentOrderByUserId(userId), OK);
   }
  
  /* main
    /**
     * UPDATE ORDER ITEM QUANTITY

     * */
    /* 
    @PatchMapping("{id}/orders/current")
    public ResponseEntity<String> updateOrderItemHandler(@PathVariable("id") int targetUserId, 
        @RequestHeader(name="userId") int userId, @RequestBody Item itemToUpdateQuantity) {
        //Make sure the Item we're trying to change in the cart exists!
        Item theRealItem;
        try{
            theRealItem = is.getItemById(itemToUpdateQuantity.getItemId());
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(NOT_FOUND);
        }

        if (itemToUpdateQuantity.getStock() < 0){ //Quantity less than 0: Return 400 (Bad Request) with information “Can’t have a quantity less than zero!” in the response body.
            return new ResponseEntity<>("Can’t have a quantity less than zero!", BAD_REQUEST);
        } else if (itemToUpdateQuantity.getStock() > theRealItem.getStock()){ // Quantity greater than Item.stock: Return 400 (Bad Request) with information "Requested quantity higher than current stock." in the response body.
            return new ResponseEntity<>("Requested quantity higher than current stock!", BAD_REQUEST);
        }

        //Authorization check: If the user ID in the header does not match the user ID in the path, return a 403 (Forbidden) status
        User loggedInUser;
        try {
            loggedInUser = us.getUserById(userId);
            return new ResponseEntity<>(FORBIDDEN); // Logged-in user ID and path parameter ID mismatch: Return 403 (Forbidden)
        }
        OrderItem updatedOrderItem /** Update the quantity of the order item */
   //             = us.editCartItemQuantity(userId, orderItem.getItem().getItemId(), orderItem.getQuantity());
   //     if(updatedOrderItem == null){
   //         return new ResponseEntity<>(NOT_FOUND); //Item not in the current order: Return 404 (Not Found)
   //     }
   //     return new ResponseEntity<>("Quantity Updated", OK);
   // }

    //Story ID 14: User Gets All of Their Items For Sale
    // This function retrieves all items for a specific user
    @GetMapping("{userId}/items")
    public ResponseEntity<List<Item>> getItemsByUserIdHandler(@PathVariable int userId, @RequestHeader("userId") int userIdHeader) {
        User user;
        List<Item> items;
        // Authorization check: If the user ID in the header does not match the user ID in the path, return a 403 (Forbidden) status
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

  //TO-DO: Check this code in an IDE
    @PatchMapping("/{userId}/orders/checkout")
    public ResponseEntity<?> cartCheckout(@PathVariable int userId, @RequestBody Order order, @RequestHeader int loggedInUser) {
        try {
            if (loggedInUser != userId) {
                return new ResponseEntity<>(FORBIDDEN);
            }


            Order approveOrder = os.cartCheckout(userId, order, loggedInUser);
            OrderItem invalidItem = os.findInvalidOrderItem(userId);

            if (invalidItem != null) {
                //approveOrder.setStatus(Order.StatusEnum.PENDING);
                return ResponseEntity.badRequest().body("Requested quantity for " + invalidItem.getItem().getName() + " higher than current stock. " +
                                                        "Current stock: "  + invalidItem.getItem().getStock());
            }

            return ResponseEntity.ok(approveOrder);
        }
        catch(NullAddressException e){
            return new ResponseEntity<>(BAD_REQUEST);
        }
        catch(UserNotFoundException e){
            System.out.println("user not found?");
            return ResponseEntity.notFound().build();
        }
    }

  /*
  * Merged in to handle in IDE
  * TO-DO: The request body should be an Item, not an OrderItem
  * This is to allow the front end to pass in an itemID and a stock for use in updating the orderItem.
  */
    @PatchMapping("/{userId}/orders/current")
    public ResponseEntity<Order> updateOrderStatusHandler(
            @PathVariable int userId,
            @RequestBody OrderItem orderItem,
            @RequestHeader("userId") int userIdHeader) {
        OrderItem updatedOrderItem;
        try {
            if (userIdHeader != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            updatedOrderItem = us.removeItemFromPendingOrder(userId, orderItem.getItem().getItemId(), orderItem.getQuantity());
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrderItem.getOrder());
        } catch (OrderProcessingException e) {
            return ResponseEntity.status(NOT_FOUND).build();
        }
    }

    @GetMapping("{userId}/orders")
    public ResponseEntity<List<Order>> viewOrderHistory(@PathVariable int userId, @RequestHeader(name="userId") int userIdHeader) {
        User user;
        List<Order> orders;
        try {
            if (userIdHeader != userId) {
                return new ResponseEntity<>(FORBIDDEN);
            }
            user = us.getUserById(userId);
            orders = us.viewOrderHistory(user.getUserId());
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(orders, OK);
    }
}
