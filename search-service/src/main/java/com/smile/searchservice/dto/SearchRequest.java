package com.smile.searchservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {
    private String title;
    private String ageRating;
    private String description;
    private String releaseDate;
    private Double rating;
    private DirectorRequest director;
    private List<ActorRequest> actors;
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