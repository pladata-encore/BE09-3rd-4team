package com.smile.review.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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
}
