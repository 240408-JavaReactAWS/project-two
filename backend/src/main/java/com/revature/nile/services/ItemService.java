package com.revature.nile.services;

import com.revature.nile.exceptions.ItemNotCreatedException;
import com.revature.nile.exceptions.ItemNotFoundExceptions;
import com.revature.nile.models.Item;
import com.revature.nile.models.User;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.OrderItemRepository;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository, OrderItemRepository orderItemRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /*This function stores an Item in the database */
    public Item addNewItem(Item item, int sellerId) throws ItemNotCreatedException, EntityNotFoundException {
        Optional<User> optionalUser = userRepository.findById(sellerId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            item.setUser(user);
            if (item.getStock() < 0)
                throw new ItemNotCreatedException("Cannot create an item with negative stock");
            if (item.getPrice() < 0)
                throw new ItemNotCreatedException("Cannot create an item with negative price");
            if (item.getName() == null || item.getName().isEmpty())
                throw new ItemNotCreatedException("Cannot create an item with no name");
            if (item.getDescription() == null || item.getDescription().isEmpty())
                throw new ItemNotCreatedException("Cannot create an item with no description");
            if (item.getImage() == null || item.getImage().isEmpty())
                throw new ItemNotCreatedException("Cannot create an item with no image");
            return itemRepository.save(item);
        }
        throw new EntityNotFoundException("User with id: " + sellerId + " doesn't exist");
    }

    public Item getItemById(int itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        }
        throw new EntityNotFoundException("Item with id: " + itemId + " doesn't exist");
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // This function retrieves all items for a specific user
    public List<Item> getItemsByUserId(int userId) {
        Optional<List<Item>> items = itemRepository.findAllByUserUserId(userId);
        if(!items.isPresent() || items.get().isEmpty()) {
            throw new ItemNotFoundExceptions("No items found for user with id: " + userId);
        }
        return items.get();
    }
}
