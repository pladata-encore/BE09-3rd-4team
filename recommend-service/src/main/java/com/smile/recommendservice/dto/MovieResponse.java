package com.smile.recommendservice.dto;

import lombok.Data;

import java.util.List;
// Feign 응답용 DTO – movie-service에서 넘어온 JSON 받는 용도
@Data
public class MovieResponse {
    private Long id;
    private String title;
    private List<String> genres;

    // 필요 없으면 생략해도 되지만 원래 응답 구조와 일치해야 문제가 없음
    private String description;
    private String ageRating;
    private Double rating;
    private String releaseDate;
    private String directorName;
    private List<String> actors;
}
