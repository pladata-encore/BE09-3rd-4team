package com.smile.searchservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    // 사용자 정보 조회
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") String id);
}
