package com.smile.movieservice.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private String ageRating;
    private List<String> genres;
    private Double rating;
    private String releaseDate;
    private String directorName;
    private List<String> actors;
}