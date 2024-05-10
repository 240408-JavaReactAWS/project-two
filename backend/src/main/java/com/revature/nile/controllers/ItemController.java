package com.revature.nile.controllers;

import com.revature.nile.exceptions.ItemNotFoundException;
import com.revature.nile.exceptions.ItemNotCreatedException;
import com.revature.nile.exceptions.ReviewNotFoundException;
import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.Review;
import com.revature.nile.models.User;
import com.revature.nile.services.ItemService;
import com.revature.nile.services.ReviewService;
import com.revature.nile.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import javax.naming.AuthenticationException;

@RestController
@RequestMapping("items")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH})
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService, ReviewService reviewService) {
        this.itemService= itemService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    /*
     * Story ID 15: User adds a new item for sale
     * This function creates a new Item and stores it in the database.
     * The function takes in the Item object and the user ID from the header for authorization.
     * The function handles an ItemNotCreatedException by returning a BAD_REQUEST status.
     * The function handles an EntityNotFoundException by returning a NOT_FOUND status.
     * On success, the function returns a CREATED status and the created Item.
     * 
     * NOTE: At present, front end should be passing an item with no specified userId. If they do,
     *     the userId will be changed to the userId in the header. In the future, we may want to validate
     *     that the Item does not contain a userID to begin with.
     */
    @PostMapping
    public ResponseEntity<Item> addNewItemHandler(@RequestBody Item item, @RequestHeader(name="userId") int userId) {
        Item newItem;
        try {
            newItem = itemService.addNewItem(item, userId);
        } catch (ItemNotCreatedException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(newItem, CREATED);
    }

    /*
     * Story ID 4: User views an item
     * This function returns an item by its ID.
     * This function does not require authorization.
     * This function handles an EntityNotFoundException by returning a NOT_FOUND status.
     * On success, the function returns an OK Status with the Item.
     */
    @GetMapping("{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable int itemId) {
        Item item;
        try {
            item = itemService.getItemById(itemId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(item, OK);
    }

    /*
     * Story ID 3: User views all items
     * This function returns all items in the database.
     * This function does not require authorization.
     * On success, the function returns an OK Status with a list of Items.
     */
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    /*
     * Story ID 13: User makes a review on an Item
     * This function creates a new Review.
     * The function takes in the item ID and the Review object.
     * Additionally, the function takes in the user ID from the header for authorization.
     * The function returns a BAD_REQUEST if the rating is not between 1 and 5.
     * The function returns a BAD_REQUEST if the text is null or empty.
     * The function handles an AuthenticationException from the itemService by returning a FORBIDDEN status.
     * On success, the function returns an OK status with the created Review.
     */
    @PostMapping("{itemId}/reviews")
    public ResponseEntity<Review> addReviewToItem(@PathVariable int itemId, @RequestHeader(name="userId") int userId, @RequestBody Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        if (review.getText() == null || review.getText().isEmpty()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(itemService.addReviewToItem(review, userId, itemId));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(FORBIDDEN);
        } catch( EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
    /*
     * Story ID 14: User Deletes One Of Their Items For Sale
     * This function deletes an item for sale
     * The function takes in the item ID
     * Additionally, the function takes in the user ID from the header for authorization.
     * The function returns a FORBIDDEN status if the user is not the seller of the item.
     * The function handles an EntityNotFoundException by returning a NOT_FOUND status.
     * On success, the function returns an OK status with the deleted item ID.
     * TO-DO: Delete requests should be idempotent, so any successful delete request should return a 200 status code.
     */
    @DeleteMapping("{itemId}")
    public ResponseEntity<Integer> deleteItemByHandler(@PathVariable int itemId, @RequestHeader("userId") int id) {
        try {
            Item item = itemService.getItemById(itemId);
            //Authorization check: User ID in the header must match the seller ID of the item
            int sellerId = item.getUser().getUserId();
            if (sellerId != id) {
                return new ResponseEntity<>(FORBIDDEN);
            }
            itemService.deleteItemOnSale(itemId);
            return new ResponseEntity<>(OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
    /*
     * Story ID 14: User Updates One Of Their Items For Sale
     * This function updates an item for sale
     * The function takes in the item ID and the updated item object.
     * Additionally, the function takes in the user ID from the header for authorization.
     * The function returns a FORBIDDEN status if the user is not the seller of the item.
     * The function handles an EntityNotFoundException by returning a NOT_FOUND status.
     * On success, the function returns an OK status.
     * TO-DO: The function should return the updated item object.
     * 
     * NOTE: At present, front end should only pass the stock field of the Item, as it is the only thing we are updating.
     * Example JSON body:
     * {
     *    "stock": 10
     * }
     */
    @PatchMapping("{itemId}")
    public ResponseEntity<Item> patchItemByHandler(@PathVariable int itemId, @RequestBody Item item, @RequestHeader("userId") int userId) {
        Item oldItem;
        // Make sure the item exists before we try to update it
        try{
            oldItem = itemService.getItemById(itemId);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(NOT_FOUND);
        }
        //Authorization check: User ID in the header must match the seller ID of the item
        if (userId != oldItem.getUser().getUserId()) {
            return new ResponseEntity<>(FORBIDDEN);
        }
        int stock = item.getStock();
        try {
            //itemService.pathItem(itemId, stock);
            return new ResponseEntity<Item>(itemService.patchItemStockById(itemId, stock), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @GetMapping("{itemId}/orders")
    public ResponseEntity<List<Order>> getOrdersByItemHandler(@PathVariable int itemId, @RequestHeader(name="userId") int userId) {
        try {
            User user = userService.getUserById(userId);
            Item item = itemService.getItemById(itemId);
            if (item.getUser().getUserId() != user.getUserId()) {
                return new ResponseEntity<>(FORBIDDEN);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        try {
            return ResponseEntity.ok(itemService.getOrdersByItem(itemId));
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }


    @GetMapping("/{itemId}/reviews")
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable int itemId) {
        try{
            return ResponseEntity.ok(reviewService.getAllReviewsByItemId(itemId));
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }

    }
}