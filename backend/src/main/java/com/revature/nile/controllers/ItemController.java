package com.revature.nile.controllers;

import com.revature.nile.services.ItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService= itemService;
    }

}
