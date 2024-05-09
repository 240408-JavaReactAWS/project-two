package com.revature.nile.services;

import com.revature.nile.models.*;
import com.revature.nile.repositories.*;
import jakarta.persistence.EntityExistsException;
import com.revature.nile.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
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

    public void pathItem(int itemId, int stock) throws EntityNotFoundException {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            item.setStock(stock);
            itemRepository.save(item);
        }
        throw new EntityNotFoundException("Item with id: " + itemId + " doesn't exist");
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Review addReviewToItem(Review review, int userId, int id) throws EntityNotFoundException, AuthenticationException {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isEmpty()) {
            throw new EntityNotFoundException("Item with id: " + id + " doesn't exist");
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with username: " + userId + " doesn't exist");
        }
        Item item = optionalItem.get();
        User user = optionalUser.get();
        boolean hasOrdered = false;
        List<Order> userOrders = user.getOrders();
        for(Order order : userOrders) {
            for(OrderItem orderItem : order.getOrderItems()) {
                if(orderItem.getItem().getItemId() == id && !order.getStatus().equals(Order.StatusEnum.PENDING)) {
                    hasOrdered = true;
                    break;
                }
            }
        }
        if (!hasOrdered) {
            throw new AuthenticationException("User has not ordered this item");
        }
        List<Review> reviews = user.getReviews();
        for (Review r : reviews) {
            if (r.getItem().getItemId() == id) {
                throw new AuthenticationException("User has already reviewed this item");
            }
        }
        review.setItem(item);
        review.setUser(user);
        return reviewRepository.save(review);
    }

    public void deleteItemOnSale(int itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ItemNotFoundException("Item not found with id: " + itemId);
        }
        itemRepository.deleteById(itemId);
    }
    
    public List<Item> getItemsByUserId(int userId) {
        Optional<List<Item>> items = itemRepository.findAllByUserUserId(userId);
        if(!items.isPresent() || items.get().isEmpty()) {
            throw new ItemNotFoundExceptions("No items found for user with id: " + userId);
        }
        return items.get();
    }

    public List<Order> getOrdersByItem(int itemId) {
        Optional<List<OrderItem>> orderItems = orderItemRepository.findAllByItemItemId(itemId);
        if(orderItems.isEmpty()) {
            throw new ItemNotFoundException("Item Id not found");
        }
        List<Order> orders = new ArrayList<Order>();
        for(OrderItem orderItem : orderItems.get()) {
            orders.add(orderItem.getOrder());
        }
        return orders;
    }
}
