package com.smile.searchservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;              // 영화 고유 ID

    private String title;         // 영화 제목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")  // 장르와의 관계 (외래키)
    private Genre genre;          // 영화 장르

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id")  // 감독과의 관계 (외래키)
    private Director director;    // 감독

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")  // 배우와의 관계 (외래키)
    private Actor actor;           // 배우

    private String description;    // 영화 설명
    private Double rating;         // 평균 평점

    private LocalDate releaseDate; // 개봉일
}