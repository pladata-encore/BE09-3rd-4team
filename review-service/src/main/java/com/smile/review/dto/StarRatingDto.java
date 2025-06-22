package com.smile.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StarRatingDto {
    private Long movieId;
    private Double rating;
    private String userId;


    public StarRatingDto(Long movieId, Double rating, String userId) {
        this.movieId = movieId;
        this.rating = rating;
        this.userId = userId;
    }
}
