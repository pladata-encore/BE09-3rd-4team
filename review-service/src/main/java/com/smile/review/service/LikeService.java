package com.smile.review.service;

import com.smile.review.domain.ReviewLike;

import com.smile.review.dto.responsedto.LikeResponseDto;
import com.smile.review.repository.like.ReviewLikeRepository;
import com.smile.review.repository.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.smile.review.service.ReviewService.reviewRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    /**
     * 토글: 이미 좋아요 있으면 취소, 없으면 추가
     */
    @Transactional
    public LikeResponseDto toggleLike(Long reviewId, String username) {

        // 1. 인증 및 유효성 체크
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("존재하지 않는 리뷰입니다: " + reviewId);
        }

        boolean exists = reviewLikeRepository.existsByReviewIdAndUsername(reviewId, username);

        if (exists) {
            reviewLikeRepository.deleteByReviewIdAndUsername(reviewId, username);
        } else {
            ReviewLike like = ReviewLike.builder()
                    .reviewId(reviewId)
                    .username(username)
                    .liked(true)
                    .build();
            reviewLikeRepository.save(like);
        }

        long count = reviewLikeRepository.countByReviewId(reviewId);

        return LikeResponseDto.builder()
                .reviewId(reviewId)
                .username(username)
                .likeCount(count)
                .liked(!exists)
                .build();
    }

    /**
     * 현재 사용자의 좋아요 상태 + 전체 좋아요 수 조회
     */
    public LikeResponseDto getLikeInfo(Long reviewId, String username) {
        boolean exists = reviewLikeRepository.existsByReviewIdAndUsername(reviewId, username);
        long count = reviewLikeRepository.countByReviewId(reviewId);
        return LikeResponseDto.builder()
                .reviewId(reviewId)
                .username(username)
                .likeCount(count)
                .liked(exists)
                .build();
    }

    /**
     * 명시적 좋아요 취소 (DELETE)
     */
    @Transactional
    public void cancelLike(Long reviewId, String username) {
        reviewLikeRepository.deleteByReviewIdAndUsername(reviewId, username);
    }
}
