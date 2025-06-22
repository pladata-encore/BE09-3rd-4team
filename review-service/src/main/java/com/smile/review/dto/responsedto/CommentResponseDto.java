package com.smile.review.dto.responsedto;

import com.smile.review.client.dto.UserDto;
import com.smile.review.domain.Comment;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 리뷰 댓글 응답용 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long reviewId;
    private String userId;
    private String userName;  // user 서비스에서 받아온 사용자 이름
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static CommentResponseDto fromEntity(Comment c, UserDto u) {
        return CommentResponseDto.builder()
                .commentId(c.getId())
                .userId(u.getUserId())
                .userName(u.getUserId())
                .content(c.getContent())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }

}
