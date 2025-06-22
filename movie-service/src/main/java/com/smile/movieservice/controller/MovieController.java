package com.smile.movieservice.controller;

import com.smile.movieservice.MovieServiceApplication;
import com.smile.movieservice.dto.MovieRequest;
import com.smile.movieservice.dto.MovieResponse;
import com.smile.movieservice.service.MovieService;
import com.smile.movieservice.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/regist")
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(@RequestBody MovieRequest request) {
        MovieResponse response = movieService.createMovie(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> updateMovie(@PathVariable Long id,
                                                                  @RequestBody MovieRequest request) {
        MovieResponse response = movieService.updateMovie(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 전체 조회 - /movies/fetch
    @GetMapping("/fetchAll")
    public ResponseEntity<ApiResponse<List<MovieResponse>>> getAllMovies() {
        List<MovieResponse> movies = movieService.getAllMovies();
        return ResponseEntity.ok(ApiResponse.success(movies));
    }

    // 단건 조회 - /movies/fetchAll/{id}
    @GetMapping("/fetchAll/{id}")
    public ResponseEntity<ApiResponse<MovieResponse>> getMovieById(@PathVariable Long id) {
        MovieResponse movie = movieService.getMovieById(id);
        System.out.println("🎥 movie = " + movie);

        return ResponseEntity.ok(ApiResponse.success(movie));
    }

    // 영화 rating 업데이트
    @PutMapping("/internal/{id}/update-average-rating")
    public ResponseEntity<Void> updateAverageRating(@PathVariable Long id) {
        movieService.recalculateAndUpdateAverageRating(id);
        return ResponseEntity.ok().build();
    }


}
