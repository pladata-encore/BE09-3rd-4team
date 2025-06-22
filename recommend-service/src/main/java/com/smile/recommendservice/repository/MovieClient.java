package com.smile.recommendservice.repository;

import com.smile.recommendservice.common.ApiResponse;
import com.smile.recommendservice.dto.MovieDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 특정 영화의 상세 정보를 가져오기 위해 movie-service의
// /movies/fetchAll/{movieId} 엔드포인트를 호출하는 FeignClient

@FeignClient(name = "movie-service", path = "/movies")
public interface MovieClient {

    @GetMapping("/fetchAll/{movieId}")
    ApiResponse<MovieDto> getMovieById(@PathVariable("movieId") Long movieId);


}
