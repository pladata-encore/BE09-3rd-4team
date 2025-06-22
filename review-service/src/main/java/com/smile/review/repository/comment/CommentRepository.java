package com.smile.review.repository.comment;

import com.smile.review.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 특정 리뷰(reviewId)에 속한 댓글 페이징 조회
     */
    Page<Comment> findByReview_ReviewId(Long reviewId, Pageable pageable);

    /**
     * 특정 리뷰의 댓글 개수 조회
     */
    Long countByReview_ReviewId(Long reviewId);

    /**
     * 예: 특정 사용자 작성 댓글만 조회
     */
    Page<Comment> findByUserId(Long userId, Pageable pageable);

    /**
     * 예: 삭제 시에는 service에서 deleteById 호출하거나,
     * deleteByReviewIdAndUserId 등 특별 메서드가 필요하면 아래처럼 정의 가능:
     */
    // void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}
