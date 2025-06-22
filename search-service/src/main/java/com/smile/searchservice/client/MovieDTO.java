package com.smile.searchservice.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;               // 영화 ID
    private String title;          // 영화 제목
    private String genre;          // 영화 장르
    private String director;       // 감독
    private String actor;          // 배우
    private String description;    // 영화 설명
    private Double rating;         // 영화 평점
    private String releaseDate;    // 개봉일
}