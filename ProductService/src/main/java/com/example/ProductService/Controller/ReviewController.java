package com.example.ProductService.Controller;

import com.example.ProductService.Dtos.ReviewReplyDto;
import com.example.ProductService.Dtos.ReviewRequest;
import com.example.ProductService.Dtos.ReviewUpdateDto;
import com.example.ProductService.Models.Review;
import com.example.ProductService.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="api/v1/review" )
@CrossOrigin
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addReview(@RequestBody ReviewRequest review) {
        reviewService.createReview(review);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviews() {
        return reviewService.getReviews();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Review> getReviewsByProductId(@PathVariable("productId") int productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PutMapping("update/reply")
    @ResponseStatus(HttpStatus.OK)
    public void replyReview(@RequestBody ReviewReplyDto review) {
        reviewService.addReply(review);
    }

    @PutMapping("update/comment")
    @ResponseStatus(HttpStatus.OK)
    public void updateReview(@RequestBody ReviewUpdateDto review) {
        reviewService.updateReview(review);
    }

}
