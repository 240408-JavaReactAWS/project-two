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
@CrossOrigin(origins = "http://localhost:3000", 
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH},
    allowCredentials = "true")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    /*
     * This function returns all reviews in the database.
     * This function does not require authorization.
     * On success, the function returns an OK status and a list of all reviews.
     * TO-DO: This function is currently not used to satisfy any user stories. Consider removing it.
     */
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /*
     * This function returns a review by its ID.
     * This function does not require authorization.
     * The function takes in the review ID.
     * On success, the function returns an OK status with the Review.
     * TO-DO: This function is currently not used to satisfy any user stories. Consider removing it.
     */
    @GetMapping("{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable int reviewId) {
        Review review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }
}
