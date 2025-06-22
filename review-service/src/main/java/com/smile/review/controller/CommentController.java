package com.smile.review.controller;

import com.smile.review.dto.requestdto.CommentRequestDto;
import com.smile.review.dto.responsedto.CommentResponseDto;
import com.smile.review.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;


@RestController
@RequestMapping("/reviews/{reviewId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /** 댓글 목록 조회 ok  */
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> listComments(
            @PathVariable Long reviewId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponseDto> page = commentService.getComments(reviewId, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(page);
    }

    /**
     * 댓글 작성 ok
     * POST /reviews/{reviewId}/comments
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long reviewId,
            @RequestBody CommentRequestDto dto,
            Principal principal) {
        String userId = (principal != null) ? principal.getName() : dto.getUserName();
        CommentResponseDto created = commentService.addComment(reviewId, userId, dto);
        if(created== null) {
            System.out.println("댓글 생성 실패! reviewId=" + reviewId + ", userId=" + userId + ", content=" + dto.getContent());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 생성에 실패했습니다. reviewId, userId, content를 확인하세요.");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()   // "/reviews/{reviewId}/comments"
                .path("/{id}")
                .buildAndExpand(created.getCommentId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }


    /**
     * 댓글 수정 ok
     * PUT /reviews/{reviewId}/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> editComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto dto,
            Principal principal) {
        String userName = principal.getName();
        CommentResponseDto updated = commentService.editComment(reviewId, commentId, userName, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 댓글 삭제 ok
     * DELETE /reviews/{reviewId}/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            Principal principal) {
        String userName = principal.getName();
        commentService.deleteComment(reviewId, commentId, userName);
        return ResponseEntity.noContent().build();
    }
}
