package com.smile.userservice.query.controller;

import com.smile.userservice.common.ApiResponse;
import com.smile.userservice.query.dto.UserDTO;
import com.smile.userservice.query.dto.UserDetailsResponse;
import com.smile.userservice.query.dto.UserModifyRequest;
import com.smile.userservice.query.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/internal/recommend/users/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserForRecommend(@PathVariable String userId) {
        UserDTO user = userQueryService.getUserForRecommend(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    // 로그인된 사용자 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserDetail(
            @AuthenticationPrincipal String userId
    ){
        UserDetailsResponse response = userQueryService.getUserDetail(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // userId를 통한 사용자 정보 조회
    @GetMapping("/internal/users/{userId}")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUser(@PathVariable("userId") String userId) {
        UserDetailsResponse response = userQueryService.getUserDetail(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // userId를 통한 사용자 정보 조회 (관리자용)
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserByAdmin(@PathVariable("userId") String userId) {
        UserDetailsResponse response = userQueryService.getUserDetail(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/internal/users/me")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserFromInternal(
            @RequestHeader("X-User-Id") String userId
    ) {
        // DB에서 gender, age 다 가져옴
        UserDetailsResponse response = userQueryService.getUserDetail(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    // 사용자 정보 수정
    @PutMapping("/users/modify")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> modifyUser(
        @AuthenticationPrincipal String userId,
        @RequestBody UserModifyRequest request
    ){
        UserDetailsResponse response = userQueryService.modifyUser(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 사용자 정보 삭제
    @DeleteMapping("/users/delete")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> deleteUser(
            @AuthenticationPrincipal String userId
    ){
        UserDetailsResponse response = userQueryService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
