package com.revature.nile.controllers;

import com.revature.nile.exceptions.ItemNotCreatedException;
import com.revature.nile.models.Item;
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

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("items")
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

    /*This function creates a new Item 
     * This is passing the seller ID as a request parameter for now.
     * In actual implementation, the sellerId should be retrieved from the header.
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

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    /*This function creates a new Review 
     * This is passing the reviewer ID as a request parameter for now.
     * In actual implementation, the reviewerId should be retrieved from the header.
     * We're not worrying about matching item IDs for now.
     */
    @PostMapping("{id}/reviews")
    public ResponseEntity<Review> addReview(@RequestBody Review review, @PathVariable int id, @RequestParam int reviewerId) {
        try {
            Review toAdd = review;
            toAdd.setItem(itemService.getItemById(id));
            toAdd.setUser(userService.getUserById(reviewerId));
            return new ResponseEntity<Review>(reviewService.addReview(review), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
