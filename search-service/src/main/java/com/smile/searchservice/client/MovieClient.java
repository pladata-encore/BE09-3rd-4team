package com.smile.searchservice.client;

import com.smile.searchservice.common.ApiResponse;
import com.smile.searchservice.dto.SearchRequest;
import com.smile.searchservice.dto.SearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "MOVIE-SERVICE")
public interface MovieClient {
    @GetMapping("/movies/fetchAll")
    ApiResponse<List<SearchResponse>> getMovies();

    @GetMapping("/movies/fetchAll/{id}")
    ApiResponse<SearchResponse> getMovie(@PathVariable("id") Long id);

    @PostMapping("/movies/regist")
    ApiResponse<SearchResponse> addMovie(@RequestBody SearchRequest movie);

    @PutMapping("/movies/modify/{id}")
    ApiResponse<SearchResponse> updateMovie(@PathVariable("id") Long id, @RequestBody SearchRequest movie);

    @DeleteMapping("/movies/delete/{id}")
    ApiResponse<Void> deleteMovie(@PathVariable("id") Long id);

    // movie-service에도 구현이 되어있어야함.
//    @GetMapping("/movies/year/{year}")
//    ApiResponse<List<SearchResponse>> getMoviesByYear(@PathVariable("year") int year);
}