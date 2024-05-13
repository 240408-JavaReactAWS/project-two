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
@CrossOrigin(origins = "http://revshop-2024.s3-website.us-east-2.amazonaws.com/", 
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH},
    allowCredentials = "true")
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
    public ResponseEntity<?> registerNewUserHandler(@RequestBody User newUser) {
        if(newUser == null || newUser.getEmail() == null || newUser.getEmail().isEmpty()
                || newUser.getUserName() == null || newUser.getUserName().isEmpty()
                || newUser.getPassword() == null || newUser.getPassword().isEmpty()
                || newUser.getAddress() == null || newUser.getAddress().isEmpty()
                || newUser.getFirstName() == null || newUser.getFirstName().isEmpty()
                || newUser.getLastName() == null ||  newUser.getLastName().isEmpty()) { //If any of the fields are empty, return a 400 (Bad Request) status
            return new ResponseEntity<>("Please fill out all fields", BAD_REQUEST);
        }
        User registerUser;
        try {
            registerUser = us.registerUser(newUser);
        } catch(UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), CONFLICT);
        }
        return new ResponseEntity<>(registerUser, CREATED);
    }

    /*
     * Story ID 2: User logs in using email and password
     * This function logs in a User and returns the User object.
     * The function takes in the User object.
     * The function handles an AuthenticationException from the UserService by returning an UNAUTHORIZED status.
     * On success, the function returns an OK status and the User object.
     */
    @PostMapping("login")
    public ResponseEntity<?> loginHandler(@RequestBody User loginAttempt) {
        User user;
        try {
            user = us.loginUser(loginAttempt);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
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
     * The front end will pass in a partially-completed Item object. It should have the ItemID and Stock (which we will use to update the quantity)
     * {
     *    "itemId": 1,
     *    "stock": 2
     * }
     * 
     * The other fields will be populated when the OrderItem is added to the database.
     */
    @PostMapping("{id}/orders/current")
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


    /*
     * TO-DO: This function returns 200 with an empty response body if the user doesn't have a cart yet.
     */
    @GetMapping("{id}/orders/current")
    public ResponseEntity<Order> viewCurrentOrdersHandler(@PathVariable int id, @RequestHeader(name="userId") int userId) {
        User loggedInUser;
        try {
            loggedInUser = us.getUserById(userId);
            if(loggedInUser.getUserId() != id){
                    return new ResponseEntity<>(FORBIDDEN); // Logged-in user ID and path parameter ID mismatch: Return 403 (Forbidden)
                }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND); 
        }

        return new ResponseEntity<>(os.getCurrentOrderByUserId(userId), OK);
   }
  
    /*
     * Story ID 7: User updates the quantity of an item in their cart.
     * This function updates the quantity of an item in the user's current order.
     * The function takes in the User ID for the user whose cart we are updating, the Item object with the updated quantity, and the logged in User ID in the header.
     *
     * The front end will pass in a partially-completed Item object. It should have the ItemID and stock (which we will use to set the quantity).
     * {
     *    "itemId": 1,
     *    "stock": 2
     * }
     * 
     */
    @PatchMapping("{id}/orders/current")
    public ResponseEntity<Order> updateOrderItemHandler(@PathVariable("id") int targetUserId, 
        @RequestHeader(name="userId") int userId, @RequestBody Item itemToUpdateQuantity) {
        //Get the User object for the user whose cart we are updating, return 404 (Not Found) if the user does not exist
        User loggedInUser;
        try {
            loggedInUser = us.getUserById(userId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        //Authorization check: If the user ID in the header does not match the user ID in the path, return a 403 (Forbidden) status
        if (targetUserId != userId){
            return new ResponseEntity<>(FORBIDDEN);
        } 
        //Make sure the Item we're trying to change in the cart exists! Return 404 (Not Found) if the item does not exist
        Item theRealItem;
        try{
            theRealItem = is.getItemById(itemToUpdateQuantity.getItemId());
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(NOT_FOUND);
        }

        //It would be really nice to send specific error information back, but we're not set up for that yet.
        //Stretch goal TO-DO: Send specific error information back to the front end
        //You can't put less than 1 item in a cart!
        if (itemToUpdateQuantity.getStock() < 0){ //Quantity less than 0: Return 400 (Bad Request) with information “Can’t have a quantity less than zero!” in the response body.
            return new ResponseEntity<>(/*"Can’t have a quantity less than zero!", */BAD_REQUEST);

        } else if (itemToUpdateQuantity.getStock() > theRealItem.getStock()){ // Quantity greater than Item.stock: Return 400 (Bad Request) with information "Requested quantity higher than current item stock." in the response body.
            return new ResponseEntity<>(/*"Requested quantity higher than current item stock!", */BAD_REQUEST);
        }

        //You can't update an item that's not in your cart! If it isn't there, return 404 (Not Found)
        OrderItem orderItem = null;
        try {
            //Get the user's current order
            Order currentOrder = os.getCurrentOrderByUserId(userId);
            //Then, find an OrderItem that has the same Item ID as the Item we're trying to update
            for(OrderItem oi : currentOrder.getOrderItems()){
                if(oi.getItem().getItemId() == itemToUpdateQuantity.getItemId()){
                    orderItem = oi;
                    break;
                }
            }
            //If we didn't find an OrderItem with the same Item ID as the Item we're trying to update, return 404 (Not Found)
            if(orderItem == null){
                return new ResponseEntity<>(NOT_FOUND);
            }
            //At this point, we have the OrderItem to be updated, so let's update it.

            //If the quantity is 0, remove the OrderItem from the Order
            if(itemToUpdateQuantity.getStock() == 0){
                currentOrder.getOrderItems().remove(orderItem);
                os.deleteOrderItem(orderItem.getOrderItemId());
            }

            //Otherwise, update the quantity of the OrderItem to the new quantity
            else{
                orderItem.setQuantity(itemToUpdateQuantity.getStock());
                //Persist the updated OrderItem to the database
                os.createOrderItem(orderItem);
            }
            //Return the updated Order
            os.createOrder(currentOrder);
            return new ResponseEntity<Order>(currentOrder, OK);
        //If we get here, we didn't find a current order for the user. Return 404 (Not Found)
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

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

    /*
     * The front-end will be passing in an incomplete Order object with shipping and billing address information.
     */
    @PatchMapping("/{userId}/orders/checkout")
    public ResponseEntity<?> cartCheckout(@PathVariable int userId, @RequestBody Order addressDetails, @RequestHeader("userId") int loggedInUser) {
        //You can't checkout another user's order!
        try {
            if (loggedInUser != userId) {
                return new ResponseEntity<>(FORBIDDEN);
            }
            User user;
            //Get the user by ID. If the user does not exist, return a 404 (Not Found) status.
            try {
                user = us.getUserById(loggedInUser);
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(NOT_FOUND);
            }
            //If the passed-in Prder's shipToAddress or billAddress is not null, update the ShipToAddress and BillAddress with the order's values
            String newShipToAddress = "";
            String newBillAddress = "";
            if(addressDetails.getShipToAddress() != null) {
                newShipToAddress = addressDetails.getShipToAddress();
            }
            if(addressDetails.getBillAddress() != null) {
                newBillAddress = addressDetails.getBillAddress();
            }
            //Grab the user's current order
            Order order;
            try {
                order = os.getCurrentOrderByUserId(user.getUserId());

                //Update the ShipToAddress and BillAddress with the new values if they exist
                if(!newShipToAddress.isEmpty()) {
                    order.setShipToAddress(newShipToAddress);
                }
                if(!newBillAddress.isEmpty()) {
                    order.setBillAddress(newBillAddress);
                }
            } catch (EntityNotFoundException e) {
                return new ResponseEntity<>(NOT_FOUND);
            }
            //Make sure the user is checking out their own cart
            if(user.getUserId() != order.getUser().getUserId()) { // user cant check out anothers cart
                return new ResponseEntity<>(FORBIDDEN);
            }
            //Can't checkout an order that's not pending!
            if(order.getStatus() != Order.StatusEnum.PENDING) { // only pending orders can be checked out
                return new ResponseEntity<>(BAD_REQUEST);
            }
            Order approvedOrder;
            try{
                approvedOrder = os.cartCheckout(userId, order, loggedInUser);
            }
            catch(OrderProcessingException e)
            {
                return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
            }
            //Send an email to the user who bought the items and the seller(s) who sold them
            es.prepareCheckoutEmail(user, approvedOrder);
            User seller;
            List<User> notifiedSellers= new ArrayList<>();
            for(OrderItem orderItem : approvedOrder.getOrderItems()){
                try{
                    seller = orderItem.getItem().getUser();
                    if(!notifiedSellers.contains(seller)) { // prevent sending multiple emails if more than 1 item from
                        es.sendNotificationToSeller(seller, approvedOrder); // same seller in order
                    }
                    notifiedSellers.add(seller);
                } catch (EntityNotFoundException e){
                    return new ResponseEntity<>(NOT_FOUND);
                }
            }

            return ResponseEntity.ok(approvedOrder);
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
  /* 
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
    }*/

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
