package com.example.ProductService.Services;

import com.example.ProductService.Dtos.ReviewRequest;
import com.example.ProductService.Models.Review;
import com.example.ProductService.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor

public class ReviewService {
    private final ReviewRepository reviewRepository;

    public void createReview(ReviewRequest reviewRequest) {
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setProductId(reviewRequest.getProductId());
        review.setReviewImageUrl(reviewRequest.getReviewImageUrl());
        review.setRating(reviewRequest.getRating());
        reviewRepository.save(review);
    }

    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByProductId(int productId) {
        return reviewRepository.findByProductId(productId);
    }

}
