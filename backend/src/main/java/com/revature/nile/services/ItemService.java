package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.OrderItemRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, OrderItemRepository orderItemRepository) {
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /*This function stores an Item in the database */
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item getItemById(int itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        }
        throw new EntityNotFoundException("Item with id: " + itemId + " doesn't exist");
    }




    public Item pathItem(int itemId, int stock) throws EntityNotFoundException {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.setStock(stock);
            return itemRepository.save(item);
            //return;
        }
        throw new EntityNotFoundException("Item with id: " + itemId + " doesn't exist");
    }


    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
