package com.smile.review.client;

import com.smile.review.client.dto.UserDetailsResponse;
import com.smile.review.client.dto.UserDto;
import com.smile.review.common.ApiResponse;
import com.smile.review.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        configuration = com.smile.review.config.FeignClientConfig.class
)
public interface UserClient {

    // 2. 내부에서 user-service를 호출하는 상황
    @GetMapping("/internal/users/{userId}")
    ApiResponse<UserDetailsResponse> getUserId(@PathVariable("userId") String userName);


}
