package com.revature.nile.services;

import com.revature.nile.exceptions.OnlyOneReviewPerUserException;
import com.revature.nile.exceptions.ReviewNotFoundException;
import com.revature.nile.models.Review;
import com.revature.nile.models.User;
import com.revature.nile.repositories.ItemRepository;
import com.revature.nile.repositories.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ItemRepository itemRepository;

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository, ItemRepository itemRepository) {
        this.reviewRepository = reviewRepository;
        this.itemRepository = itemRepository;
    }

    public List<Review> getAllReviews(int itemId) {
        Optional<List<Review>> reviews = reviewRepository.findAllByItemId(itemId);
        if (reviews.isPresent()) {
            return reviews.get();
        } else {
            throw new ReviewNotFoundException("No reviews found for item with id: " + itemId);
        }
    }

    public Review addReview(Review review) {

        List<Review> listOfReviews = reviewRepository.findAll();

        if(listOfReviews.contains(review.getUser())){
            throw new OnlyOneReviewPerUserException("Only one review per user is allowed");
        }

        return reviewRepository.save(review);

    }
}
