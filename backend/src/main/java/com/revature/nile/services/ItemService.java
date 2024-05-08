package com.revature.nile.services;

import com.revature.nile.models.Item;
import com.revature.nile.models.Review;
import com.revature.nile.models.User;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.ReviewRepository;
import com.revature.nile.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public Item getItemById(int itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        }
        throw new EntityNotFoundException("Item with id: " + itemId + " doesn't exist");
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
