package com.smile.searchservice.dto;

import com.smile.searchservice.entity.Movie;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchDTO {

    private Long id;
    private String title;
    private String genre;
    private String director;
    private String actor;
    private String description;
    private Double rating;
    private String releaseDate;

    // Movie 엔티티 → DTO 변환
    public static SearchDTO from(Movie movie) {
        return SearchDTO.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre().getName())
                .director(movie.getDirector().getName())
                .actor(movie.getActor().getName())
                .description(movie.getDescription())
                .rating(movie.getRating())
                .releaseDate(movie.getReleaseDate().toString())
                .build();
    }
}