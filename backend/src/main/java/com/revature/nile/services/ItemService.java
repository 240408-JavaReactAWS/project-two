package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.repositories.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ItemService {


    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    public Item getItemById(int id) throws EntityNotFoundException {
        Optional<Item> item = itemRepository.findById(id);
        if(item.isPresent()){
            return item.get();
        }
        throw new EntityNotFoundException("Item with " + id + " doesn't exist");    }
}
