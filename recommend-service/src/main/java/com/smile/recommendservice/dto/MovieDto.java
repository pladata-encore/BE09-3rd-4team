package com.smile.recommendservice.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private String ageRating;
    private List<String> genres;
    private Double rating;
    private String releaseDate;
    private String directorName;
    private List<String> actors;

    // ✅ 추천 알고리즘에서 사용할 평균 평점 필드 (review-service 기반으로 계산)
    private double averageRating;
}
