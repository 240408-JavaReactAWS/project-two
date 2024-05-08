package com.revature.nile.controllers;

import com.revature.nile.models.Item;
import com.revature.nile.services.ItemService;
import com.revature.nile.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;

    @Autowired
    public ItemController(ItemService itemService, UserService userService) {
        this.itemService= itemService;
        this.userService = userService;
    }

    /*This function creates a new Item 
     * This is passing the seller ID as a request parameter for now.
     * In actual implementation, the sellerId should be retrieved from the header.
     */
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item, @RequestParam int sellerId) {
        Item newItem = itemService.createItem(item);
        newItem.setUser(userService.getUserById(sellerId));
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
}
