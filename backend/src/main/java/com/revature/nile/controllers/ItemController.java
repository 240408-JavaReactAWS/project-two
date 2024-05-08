package com.revature.nile.controllers;

import com.revature.nile.models.Item;
import com.revature.nile.models.Review;
import com.revature.nile.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService= itemService;
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

    @PostMapping("{itemId}/reviews")
    public ResponseEntity<Review> addReviewToItem(@PathVariable int itemId, @RequestHeader(name="userId") int userId, @RequestBody Review review) {
        try {
            return ResponseEntity.ok(itemService.addReviewToItem(review, userId, itemId));
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }
}
