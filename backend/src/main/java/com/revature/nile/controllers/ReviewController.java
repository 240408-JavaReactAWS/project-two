package com.revature.nile.controllers;

import com.revature.nile.models.Review;
import com.revature.nile.services.ReviewService;
import com.revature.nile.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;
import java.util.List;

@RestController
@RequestMapping("reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /*This function creates a new Review 
     * This is passing the reviewer ID as a request parameter for now.
     * In actual implementation, the reviewerId should be retrieved from the header.
     * We're not worrying about matching item IDs for now.
     */
    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review, @RequestParam int reviewerId) {
        try {
            Review toAdd = review;
            toAdd.setUser(userService.getUserById(reviewerId));
            return ResponseEntity.ok(reviewService.addReview(review));
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable int reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }
}
