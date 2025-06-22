package com.smile.movieservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "genre")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // 역방향 연관관계 (선택사항, 필요하면)
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;
}
