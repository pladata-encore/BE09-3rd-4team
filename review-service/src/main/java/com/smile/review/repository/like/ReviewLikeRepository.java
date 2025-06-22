package com.smile.review.repository.like;

import com.smile.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByReviewIdAndUsername(Long reviewId, String username);
    long countByReviewId(Long reviewId);
    void deleteByReviewIdAndUsername(Long reviewId, String username);
}
