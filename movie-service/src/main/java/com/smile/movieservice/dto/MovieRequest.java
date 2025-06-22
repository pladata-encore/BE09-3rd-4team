package com.smile.movieservice.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String ageRating;
    private String description;
    private String releaseDate;
    private Double rating;
    private DirectorRequest director;  // 추가
    private List<ActorRequest> actors;  // 추가
    private List<Long> genreIds;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DirectorRequest {
        private String name;
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActorRequest {
        private String name;
    }
}
