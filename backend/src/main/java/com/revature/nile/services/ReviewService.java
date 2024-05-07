package com.revature.nile.services;

import com.revature.nile.exceptions.OnlyOneReviewPerUserException;
import com.revature.nile.models.Review;
import com.revature.nile.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review addReview(Review review) {
        List<Review> listOfReviews = reviewRepository.findAll();

        // if (listOfReviews.contains(review.getUser())) {
        //     throw new OnlyOneReviewPerUserException("Only one review per user is allowed");
        // }

        return reviewRepository.save(review);
    }
}
