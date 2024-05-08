package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.Order;
import com.revature.nile.models.Review;
import com.revature.nile.models.User;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.OrderItemRepository;

import com.revature.nile.repositories.ReviewRepository;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Review addReviewToItem(Review review, int userId, int id) {
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
        AtomicBoolean hasOrdered = new AtomicBoolean(false);
        user.getOrders().forEach(order -> {
            order.getOrderItems().forEach(orderItem -> {
                if (orderItem.getItem().getItemId() == id && !order.getStatus().equals(Order.StatusEnum.PENDING)) {
                    hasOrdered.set(true);
                }
            });
        });
        if (!hasOrdered.get()) {
            throw new EntityExistsException("User has not ordered this item");
        }
        List<Review> reviews = user.getReviews();
        for (Review r : reviews) {
            if (r.getItem().getItemId() == id) {
                throw new EntityExistsException("User has already reviewed this item");
            }
        }
        review.setItem(item);
        review.setUser(user);
        return reviewRepository.save(review);
    }
}
