package com.smile.review.controller;


import com.smile.review.dto.responsedto.LikeResponseDto;
import com.smile.review.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/reviews/{reviewId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /** 좋아요 토글 (POST)  ok */
    @PostMapping
    public ResponseEntity<LikeResponseDto> toggle(
            @PathVariable Long reviewId,
            Principal principal) {
        System.out.println("리뷰 ID: " + reviewId + ", 사용자: " + (principal == null ? "null" : principal.getName()));
        LikeResponseDto dto = likeService.toggleLike(reviewId, principal.getName());
        return ResponseEntity.ok(dto);
    }

    /** 좋아요 조회 (GET)  ok */
    @GetMapping
    public ResponseEntity<LikeResponseDto> info(
            @PathVariable Long reviewId,
            Principal principal) {
        LikeResponseDto dto = likeService.getLikeInfo(reviewId, principal.getName());
        return ResponseEntity.ok(dto);
    }

    /** 좋아요 취소 (DELETE) ok */
    @DeleteMapping
    public ResponseEntity<Void> cancel(
            @PathVariable Long reviewId,
            Principal principal) {
        likeService.cancelLike(reviewId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
