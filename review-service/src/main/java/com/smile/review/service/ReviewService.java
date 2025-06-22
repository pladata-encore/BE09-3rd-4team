package com.smile.review.service;

import com.smile.review.client.MovieClient;
import com.smile.review.client.UserClient;

import com.smile.review.client.dto.MovieDto;
import com.smile.review.client.dto.UserDto;

import com.smile.review.domain.Review;
import com.smile.review.dto.StarRatingDto;
import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;


import com.smile.review.repository.review.ReviewRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


public interface ReviewService {

    ReviewResponseDto getReviewId(Long reviewId, Long movieId);

    MovieClient movieClient = null;
    UserClient userClient = null;
    ReviewRepository reviewRepository = null;
    // ... 기타 의존성 주입

    @Transactional
    ReviewResponseDto createReview(String userId, Long movieId, String content, double rating);

    @Transactional(readOnly = true)
    Page<ReviewResponseDto> listReviews(int page, int size, String sortBy);

    @Transactional
    Page<ReviewResponseDto> findReviews(String genre, Double rating, String sort, Pageable pageable);

    @Transactional
    ReviewResponseDto editReview(Long reviewId, String userName, ReviewRequestDto dto);

    @Transactional
    void deleteReview(Long reviewId, String userId);

    List<StarRatingDto> getByAgeGroup(String ageGroup);

    List<StarRatingDto> getByGender(String gender);

    List<StarRatingDto> getByAgeAndGender(String ageGroup, String gender);



    List<Double> getRatingsByMovieId(Long movieId);


}


