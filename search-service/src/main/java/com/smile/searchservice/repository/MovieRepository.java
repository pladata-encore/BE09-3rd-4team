package com.smile.searchservice.repository;

import com.smile.searchservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitle(String title);  // 영화 제목으로 검색
}