package com.revature.nile.controllers;

import com.revature.nile.models.Item;
import com.revature.nile.models.Review;
import com.revature.nile.services.ItemService;
import com.revature.nile.services.ReviewService;
import com.revature.nile.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

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
    public ResponseEntity<Item> createItem(@RequestBody Item item, @RequestParam int sellerId) {
        Item newItem = item;
        newItem.setUser(userService.getUserById(sellerId));
        return new ResponseEntity<>(itemService.createItem(newItem), CREATED);
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
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
