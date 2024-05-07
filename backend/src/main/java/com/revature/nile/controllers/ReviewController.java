package com.revature.nile.controllers;

import com.revature.nile.models.Review;
import com.revature.nile.services.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable int itemId) {
        try{
            return ResponseEntity.ok(reviewService.getAllReviews(itemId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        try {
            return ResponseEntity.ok(reviewService.addReview(review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
