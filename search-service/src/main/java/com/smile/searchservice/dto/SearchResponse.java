package com.smile.searchservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponse {
    private Long id;
    private String title;
    private String description;
    private String ageRating;          // ✅ 시청 연령 필드 추가
    private List<String> genres;
    private Double rating;
    private String releaseDate;
    private String directorName;
    private List<String> actors;
}