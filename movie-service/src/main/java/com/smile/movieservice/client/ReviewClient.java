package com.smile.movieservice.client;

import com.smile.movieservice.common.ApiResponse;
import com.smile.movieservice.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "review-service", path = "/reviews", configuration = FeignClientConfig.class)
public interface ReviewClient {

    @GetMapping("/internal/movie/{movieId}/average-rating")
    Double getAverageRatingByMovieId(@PathVariable("movieId") Long movieId);

    @GetMapping("/internal/movie/{movieId}/ratings")
    List<Double> getRatingsByMovieId(@PathVariable("movieId") Long movieId);
}

