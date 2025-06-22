package com.smile.searchservice.repository;

import com.smile.searchservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Movie, Long> {

    // 배우 이름으로 영화 검색
    List<Movie> findByActor_Name(String name); // 수정: Actor 엔티티의 name 필드 기준

    // 감독 이름으로 영화 검색
    List<Movie> findByDirector_Name(String name); // 수정: Director 엔티티의 name 필드 기준

    // 영화 제목으로 검색
    List<Movie> findByTitle(String title); // 유지

    // 장르로 영화 검색
    List<Movie> findByGenre_Name(String name); // 수정: Genre 엔티티의 name 필드 기준

    // 장르별 평점 내림차순 영화 조회
    List<Movie> findByGenreNameOrderByRatingDesc(String genre); // 유지

    // 평점 내림차순으로 모든 영화 조회
    List<Movie> findAllByOrderByRatingDesc(); // 유지

    // 개봉일 내림차순으로 모든 영화 조회
    List<Movie> findAllByOrderByReleaseDateDesc(); // 유지
}