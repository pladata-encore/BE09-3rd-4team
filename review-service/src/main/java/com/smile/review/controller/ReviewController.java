package com.smile.review.controller;


import com.smile.review.domain.Review;
import com.smile.review.dto.StarRatingDto;
import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;
import com.smile.review.service.CommentService;
import com.smile.review.service.LikeService;
import com.smile.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.smile.review.service.ReviewService.reviewRepository;


@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    public ReviewController(ReviewService reviewService,
                            CommentService commentService,
                            LikeService likeService) {
        this.reviewService = reviewService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    /**
     * 리뷰 작성 ok
     * POST /reviews
     * @param reviewRequestDto { movieId, content, rating }
     * @param principal        로그인 사용자 정보
     * @return 201 Created + Location 헤더 + 생성된 ReviewResponseDto
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@RequestBody ReviewRequestDto req
         , @AuthenticationPrincipal String userId
    ) {
             System.out.println("Controller 진입 username: " + userId);
        return ResponseEntity.ok(reviewService.createReview(
                userId, req.getMovieId(), req.getContent(), req.getRating()
        ));

    }

    /**
     * 리뷰 목록 조회 (페이징·정렬·필터링) ok
     * GET /reviews?genre=action&rating=4&sort=rating
     *
     * @param genre   (선택) 장르 필터
     * @param rating  (선택) 평점 필터: 예: 4 → 평점이 4 이상 혹은 정확히 4 (서비스 사양에 맞춰)
     * @param sort    (선택) 정렬 기준: "rating" 또는 "createdAt". 없으면 default 최신순(createdAt desc)
     * @param pageable Spring Data Pageable: page, size, sort 파라미터가 있으면 반영
     */
    @GetMapping("/list")
    public ResponseEntity<Page<ReviewResponseDto>> listReviews(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "rating", required = false) Double rating,
            @RequestParam(value = "sort", required = false) String sort,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<ReviewResponseDto> page = reviewService.findReviews(genre, rating, sort, pageable);
            return ResponseEntity.ok(page);
        } catch (Exception e) {
            // 로그 찍기
            e.printStackTrace();
            // 적절한 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Page.empty(pageable));
        }
    }

    /**
     * 특정 리뷰 조회 ok
     * GET /reviews/{reviewId}
     *
     */
    @GetMapping("/{reviewId}/{movieId}")
    public ResponseEntity<ReviewResponseDto> getReview(
            @PathVariable Long reviewId, @PathVariable Long movieId ) {
        ReviewResponseDto dto = reviewService.getReviewId(reviewId, movieId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 리뷰 수정 (PUT) ok
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> editReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequestDto req,
            @AuthenticationPrincipal String userId
    ) {
        ReviewResponseDto result = reviewService.editReview(reviewId, userId, req);
        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 삭제 (DELETE) ok
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal String userId
    ) {
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }


    /**
     * 연령대 기반 리뷰 조회
     */
    @GetMapping("/stars/age-group/{ageGroup}")
    public List<StarRatingDto> getByAgeGroup(@PathVariable String ageGroup) {
        return reviewService.getByAgeGroup(ageGroup);
    }

    /**
     * 성별 기반 리뷰 조회
     */
    @GetMapping("/stars/gender/{gender}")
    public List<StarRatingDto> getByGender(@PathVariable String gender) {
        return reviewService.getByGender(gender);
    }

    /**
     * 연령대 + 성별 기반 리뷰 조회
     */
    @GetMapping("/stars/age-group/{ageGroup}/gender/{gender}")
    public List<StarRatingDto> getByAgeAndGender(@PathVariable String ageGroup,
                                                 @PathVariable String gender) {
        return reviewService.getByAgeAndGender(ageGroup, gender);
    }

    @GetMapping("/internal/movie/{movieId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByMovieId(@PathVariable Long movieId) {
        Double avg = reviewRepository.findAverageRatingByMovieId(movieId);
        return ResponseEntity.ok(avg != null ? avg : 0.0);
    }

    @GetMapping("/internal/movie/{movieId}/ratings")
    public ResponseEntity<List<Double>> getRatingsByMovieId(@PathVariable Long movieId) {
        List<Double> ratings = reviewService.getRatingsByMovieId(movieId);
        return ResponseEntity.ok(ratings);
    }




}
