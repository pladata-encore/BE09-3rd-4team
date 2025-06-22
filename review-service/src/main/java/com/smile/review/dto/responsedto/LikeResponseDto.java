package com.smile.review.dto.responsedto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LikeResponseDto {
    private Long reviewId;    // 필드명이 reviewId 여야 .reviewId(...) 가 생깁니다
    private String username;
    private long likeCount;
    private boolean liked;
}
