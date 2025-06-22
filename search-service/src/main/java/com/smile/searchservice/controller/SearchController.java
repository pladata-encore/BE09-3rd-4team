package com.smile.searchservice.controller;

import com.smile.searchservice.common.ApiResponse; // ApiResponse import는 유지
import com.smile.searchservice.dto.SearchRequest;
import com.smile.searchservice.dto.SearchResponse; // SearchResponse import는 유지
import com.smile.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * 엔드포인트를 정의하는 클래스
 * 모든 URI 앞에 /api/v1/search/movies (주석에 따라 수정)
 */

@RestController
@RequestMapping("/search/movies") // 요청 경로
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // 영화 등록
    // POST /search/movies
    @PostMapping
    public ApiResponse<SearchResponse> createMovie(@RequestBody SearchRequest request) {
        return ApiResponse.success(searchService.createMovie(request));
    }

    // 영화 수정
    // PUT /search/movies/{id}
    @PutMapping("/{id}")
    public ApiResponse<SearchResponse> updateMovie(@PathVariable Long id, @RequestBody SearchRequest request) {
        return ApiResponse.success(searchService.updateMovie(id, request));
    }

    // 영화 삭제
    // DELETE /search/movies/{id}
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMovie(@PathVariable Long id) {
        searchService.deleteMovie(id);
        return ApiResponse.success(null);
    }

    // 배우별 영화 검색
    @GetMapping("/actor/{actorName}")
    public ApiResponse<List<SearchResponse>> getMoviesByActor(@PathVariable String actorName) {
        return ApiResponse.success(searchService.getMoviesByActor(actorName));
    }

    // 감독별 영화 검색
    @GetMapping("/director/{directorName}")
    public ApiResponse<List<SearchResponse>> getMoviesByDirector(@PathVariable String directorName) {
        return ApiResponse.success(searchService.getMoviesByDirector(directorName));
    }

    // 제목으로 영화 검색
    @GetMapping("/title/{title}")
    public ApiResponse<List<SearchResponse>> getMoviesByTitle(@PathVariable String title) {
        return ApiResponse.success(searchService.getMoviesByTitle(title));
    }

    // 장르별로 검색
    @GetMapping("/genre/{genre}")
    public ApiResponse<List<SearchResponse>> getMoviesByGenre(@PathVariable String genre) {
        return ApiResponse.success(searchService.getMoviesByGenre(genre));
    }

    // 키워드 검색 (제목, 장르, 감독, 배우 포함)
    // GET /search/movies/keyword/인셉션
    @GetMapping("/keyword/{keyword}")
    public ApiResponse<List<SearchResponse>> searchMoviesByKeyword(@PathVariable String keyword) {
        return ApiResponse.success(searchService.searchMoviesByKeyword(keyword));
    }

    // 장르별 평점순 정렬
    // GET /search/movies/genre/액션/rating
    @GetMapping("/genre/{genre}/rating")
    public ApiResponse<List<SearchResponse>> getMoviesByGenreAndRatingDesc(@PathVariable String genre) {
        return ApiResponse.success(searchService.getMoviesByGenreAndRatingDesc(genre));
    }

    // 전체 평점순 정렬
    // GET /search/movies/rating/all
    @GetMapping("/rating/all")
    public ApiResponse<List<SearchResponse>> getAllMoviesByRatingDesc() {
        return ApiResponse.success(searchService.getAllMoviesByRatingDesc());
    }

    // 최신순 정렬
    // GET /search/movies/latest
    @GetMapping("/latest")
    public ApiResponse<List<SearchResponse>> getAllMoviesByReleaseDateDesc() {
        return ApiResponse.success(searchService.getAllMoviesByReleaseDateDesc());
    }

    // 전체 영화 목록
    // GET /search/movies/all
    @GetMapping("/all")
    public ApiResponse<List<SearchResponse>> getAllMovies() {
        return ApiResponse.success(searchService.getAllMovies());
    }

    // 단건 조회
    // GET /search/movies/id/3
    @GetMapping("/id/{id}")
    public ApiResponse<SearchResponse> getMovieById(@PathVariable Long id) {
        return ApiResponse.success(searchService.getMovieById(id));
    }



    // 설명 / 줄거리 키워드 검색
    // GET /search/movies/description?keyword=마법
    @GetMapping("/description")
    public ApiResponse<List<SearchResponse>> getMoviesByDescriptionKeyword(
            @RequestParam String keyword
    ) {
        return ApiResponse.success(searchService.getMoviesByDescriptionKeyword(keyword));
    }

    // 연도별 검색
    // GET /search/movies/year/{year}
    @GetMapping("/year/{year}")
    public ApiResponse<List<SearchResponse>> getMoviesByYear(@PathVariable int year) {
        return ApiResponse.success(searchService.getMoviesByYear(year));
    }

    /*
     * 평점 min 이상 영화
     * GET /search/movies/rating-over?min=8.5
     */
    @GetMapping("/rating-over")
    public ApiResponse<List<SearchResponse>> getMoviesByMinRating(@RequestParam Double min) {
        return ApiResponse.success(searchService.getMoviesByMinRating(min));
    }

    /*
     * 제목/감독/설명 중 하나라도 keyword 포함된 영화 검색
     * GET /search/movies/keyword-all?keyword=우주
     */
    @GetMapping("/keyword-all")
    public ApiResponse<List<SearchResponse>> getMoviesByAnyFieldKeyword(@RequestParam String keyword) {
        return ApiResponse.success(searchService.getMoviesByAnyFieldKeyword(keyword));
    }

    /*
     * 최신 영화 한 편 (개봉일 기준)
     * GET /search/movies/latest-one
     */
    @GetMapping("/latest-one")
    public ApiResponse<SearchResponse> getLatestMovie() {
        return ApiResponse.success(searchService.getLatestMovie());
    }

    /*
     * 가장 오래된 영화 한 편 (개봉일 기준)
     * GET /search/movies/oldest-one
     */
    @GetMapping("/oldest-one")
    public ApiResponse<SearchResponse> getOldestMovie() {
        return ApiResponse.success(searchService.getOldestMovie());
    }

    // 특정 단어로 시작/끝나는 영화 제목
    // GET /search/movies/title-start?start=해
    @GetMapping("/title-start")
    public ApiResponse<List<SearchResponse>> getMoviesByTitleStart(@RequestParam String start) {
        return ApiResponse.success(searchService.getMoviesByTitleStart(start));
    }

    // 특정 단어로 끝나는 영화 제목
// GET /search/movies/title-end?end=길
    @GetMapping("/title-end")
    public ApiResponse<List<SearchResponse>> getMoviesByTitleEnd(@RequestParam String end) {
        return ApiResponse.success(searchService.getMoviesByTitleEnd(end));
    }

    // 감독명 또는 배우명에 특정 단어가 포함된 영화
    // GET /search/movies/any-person?keyword=박
    @GetMapping("/any-person")
    public ApiResponse<List<SearchResponse>> getMoviesByAnyPerson(@RequestParam String keyword) {
        return ApiResponse.success(searchService.getMoviesByAnyPerson(keyword));
    }

    // 전체 영화 개수
    @GetMapping("/count")
    public ApiResponse<Long> getMovieCount() {
        return ApiResponse.success(searchService.getMovieCount());
    }

    // 특정 연령 등급(시청 등급)으로만 영화 검색
    // GET /search/movies/age-rating?rating=15세 관람가
    @GetMapping("/age-rating")
    public ApiResponse<List<SearchResponse>> getMoviesByAgeRating(@RequestParam String rating) {
        return ApiResponse.success(searchService.getMoviesByAgeRating(rating));
    }

    // 여러 연령 등급을 한 번에 필터링
    // GET /search/movies/age-rating/multi?ratings=12세이상관람가,15세관람가
    @GetMapping("/age-rating/multi")
    public ApiResponse<List<SearchResponse>> getMoviesByMultipleAgeRatings(@RequestParam List<String> ratings) {
        return ApiResponse.success(searchService.getMoviesByMultipleAgeRatings(ratings));
    }

    // 특정 연령 등급 _ 평점/최신순 등 조합 필터
    // GET /search/movies/age-rating/top?rating=15세관람가&n=5
    @GetMapping("/age-rating/top")
    public ApiResponse<List<SearchResponse>> getTopMoviesByAgeRating(
            @RequestParam String rating,
            @RequestParam(defaultValue = "5") int n
    ) {
        return ApiResponse.success(searchService.getTopMoviesByAgeRating(rating, n));
    }

    // 연령별 + 장르별 동시 필터
    // GET /search/movies/age-genre?rating=15세이상관람가&genre=액션
    @GetMapping("/age-genre")
    public ApiResponse<List<SearchResponse>> getMoviesByAgeAndGenre(
            @RequestParam String rating,
            @RequestParam String genre) {
        return ApiResponse.success(searchService.getMoviesByAgeAndGenre(rating, genre));
    }

    // 제목에서 특정 단어 들어가면 필터
    // GET /search/movies/title-contains?keyword=인
    @GetMapping("/title-contains")
    public ApiResponse<List<SearchResponse>> getMoviesByTitleContains(@RequestParam String keyword) {
        return ApiResponse.success(searchService.getMoviesByTitleContains(keyword));
    }

    // 장르에 키워드 하나라도 포함 검색
    // GET /search/movies/genre-contains?keyword=로맨스
    @GetMapping("/genre-contains")
    public ApiResponse<List<SearchResponse>> getMoviesByGenreContains(@RequestParam String keyword) {
        return ApiResponse.success(searchService.getMoviesByGenreContains(keyword));
    }

    // 배우에 키워드 하나라도 포함 검색
    // GET /search/movies/actor-contains?keyword=수빈
    @GetMapping("/actor-contains")
    public ApiResponse<List<SearchResponse>> getMoviesByActorContains(@RequestParam String keyword) {
        return ApiResponse.success(searchService.getMoviesByActorContains(keyword));
    }
    
}
