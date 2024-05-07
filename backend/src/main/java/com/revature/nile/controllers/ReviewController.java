package com.revature.nile.controllers;

import com.revature.nile.models.Review;
import com.revature.nile.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;
import java.util.List;

@RestController
@RequestMapping("reviews")
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        try {
            return ResponseEntity.ok(reviewService.addReview(review));
        } catch (Exception e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }


}
