package com.smile.review.dto.responsedto;

import com.smile.review.domain.Review;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private String userId;             // user 서비스에서 가져온 정보
    private String userName;      // user 서비스에서 가져온 정보
    private Long movieId;            // movie 서비스에서 가져온 정보
    private String movieTitle;    // movie 서비스에서 가져온 정보
    private String content;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private Long likeCount;


    public static ReviewResponseDto fromEntity(Review review, String userName, String movieTitle) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUserId())
                .userName(userName)
                .movieId(review.getMovieId())
                .movieTitle(movieTitle)
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .updateAt(review.getUpdatedAt())
                .build();
    }
}
