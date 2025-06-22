package com.smile.searchservice.repository;

import com.smile.searchservice.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByName(String name);  // 장르 이름으로 검색
}