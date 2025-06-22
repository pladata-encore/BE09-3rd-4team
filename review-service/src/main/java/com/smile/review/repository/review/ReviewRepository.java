package com.smile.review.repository.review;

import com.smile.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * 특정 영화(movieId)의 리뷰 페이징 조회
     */
    Page<Review> findByMovieId(Long movieId, Pageable pageable);

    /**
     * 영화 ID 목록에 속하는 리뷰 페이징 조회 (예: 장르 필터 후 여러 movieId 조회)
     */
    Page<Review> findByMovieIdIn(List<Long> movieIds, Pageable pageable);

    /**
     * 평점(rating) 조건 조회: rating이 정확히 특정 값인 경우
     */
    Page<Review> findByRating(Double rating, Pageable pageable);

    /**
     * 평점 이상(rating >= value) 조회
     */
    Page<Review> findByRatingGreaterThanEqual(Double rating, Pageable pageable);

    /**
     * 작성자(userId) 기준 조회
     */
    Page<Review> findByUserId(String userId, Pageable pageable);

    /**
     * 예: 특정 사용자와 특정 영화 조합으로 이전에 리뷰를 남겼는지 확인할 때
     */
    boolean existsByUserIdAndMovieId(String userId, Long movieId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movieId = :movieId")
    Double findAverageRatingByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT r.rating FROM Review r WHERE r.movieId = :movieId")
    List<Double> findRatingsByMovieId(@Param("movieId") Long movieId);
}
