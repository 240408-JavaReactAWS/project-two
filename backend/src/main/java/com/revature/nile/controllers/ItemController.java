package com.revature.nile.controllers;

import com.revature.nile.exceptions.ReviewNotFoundException;
import com.revature.nile.models.Item;
import com.revature.nile.models.Review;
import com.revature.nile.services.ItemService;
import com.revature.nile.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.*;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;

    private final ReviewService reviewService;

    @Autowired
    public ItemController(ItemService itemService, ReviewService reviewService) {
        this.itemService= itemService;
        this.reviewService = reviewService;
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Item> getItemById(int itemId) {
        Item item;
        try {
            item = itemService.getItemById(itemId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(item, OK);
    }

    @GetMapping("/{itemId}/review")
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable int itemId) {
        try{
            return ResponseEntity.ok(reviewService.getAllReviewsByItemId(itemId));
        } catch (ReviewNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }

    }
}
