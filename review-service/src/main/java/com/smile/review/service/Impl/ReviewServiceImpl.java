
package com.smile.review.service.Impl;

import com.smile.review.client.MovieClient;
import com.smile.review.client.UserClient;
import com.smile.review.client.dto.MovieDto;
import com.smile.review.client.dto.UserDto;
import com.smile.review.common.ApiResponse;
import com.smile.review.domain.Review;

import com.smile.review.dto.StarRatingDto;
import com.smile.review.dto.requestdto.ReviewRequestDto;
import com.smile.review.dto.responsedto.ReviewResponseDto;


import com.smile.review.repository.review.ReviewRepository;
import com.smile.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserClient userClient;
    private final MovieClient movieClient;

    @Override
    @Transactional
    public ReviewResponseDto createReview(String userId, Long movieId, String content , double rating) {

        // 사용자 조회
        UserDto userDto;
        try {
            userDto = userClient.getUserId(userId).getData().getUser();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userId, ex);
        }
        if (userDto == null || userDto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userId);
        }

        // 영화 조회/검증
        MovieDto movieDto;
        try {
            movieDto = movieClient.getMovieId(movieId).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("영화 정보를 가져올 수 없습니다: movieId=" + movieId, ex);
        }
        if (movieDto == null || movieDto.getId() == null) {
            throw new IllegalArgumentException("유효하지 않은 영화 ID: " + movieId);
        }

        // 엔티티 생성·저장
        Review review = new Review();
        review.setUserId(userId);
        review.setMovieId(movieId);
        review.setContent(content);
        review.setRating(rating);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);

        try {
            movieClient.updateAverageRating(movieId);
            System.out.println("[연결 확인] movieClient 호출 성공");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("[오류] movieClient 호출 실패: " + ex.getMessage());
        }
        // 4) DTO 변환 후 반환
        return ReviewResponseDto.fromEntity(saved, userDto.getUserName(), movieDto.getTitle());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewId(Long reviewId, Long movieId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));

        // 작성자 정보 조회
        UserDto userDto;
        try {
            userDto = userClient.getUserId(review.getUserId()).getData().getUser();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: id=" + review.getUserId(), ex);
        }
        // 영화 정보 조회
        MovieDto movieDto;
        try {
            movieDto = movieClient.getMovieId(review.getMovieId()).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("영화 정보를 가져올 수 없습니다: id=" + review.getMovieId(), ex);
        }

        return ReviewResponseDto.fromEntity(review, userDto.getUserName(), movieDto.getTitle());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> listReviews(int page, int size, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        if ("rating".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "rating");
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        return reviewRepository.findAll(pageable)
                .map(review -> {
                    UserDto userDto = userClient.getUserId(review.getUserId()).getData().getUser();
                    MovieDto movieDto = movieClient.getMovieId(review.getMovieId()).getData();
                    return ReviewResponseDto.fromEntity(
                            review,
                            userDto.getUserName(),
                            movieDto.getTitle()
                    );
                });
    }

    @Override
    @Transactional
    public Page<ReviewResponseDto> findReviews(String genre, Double rating, String sort, Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(review -> {
                    UserDto userDto = userClient.getUserId(review.getUserId()).getData().getUser();
                    MovieDto movieDto = movieClient.getMovieId(review.getMovieId()).getData();
                    return ReviewResponseDto.fromEntity(
                            review,
                            userDto.getUserName(),
                            movieDto.getTitle()
                    );
                });
    }


    @Override
    @Transactional
    public ReviewResponseDto editReview(Long reviewId, String userName, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));

        // 소유권 확인
        UserDto userDto;
        try {
            userDto = userClient.getUserId(review.getUserId()).getData().getUser();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userName, ex);
        }
        if (userDto == null || userDto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userName);
        }
        if (!review.getUserId().equals(userDto.getUserId())) {
            throw new AccessDeniedException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 수정할 내용 반영 (movieId 변경을 허용하지 않는다면 movieId 체크 생략)
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setUpdatedAt(LocalDateTime.now());
        Review updated = reviewRepository.save(review);

        // 최신 사용자/영화 정보
        MovieDto movieDto;
        try {
            movieDto = movieClient.getMovieId(review.getMovieId()).getData();
        } catch (Exception ex) {
            throw new IllegalArgumentException("영화 정보를 가져올 수 없습니다: id=" + updated.getMovieId(), ex);
        }
        return ReviewResponseDto.fromEntity(updated, userDto.getUserName(), movieDto.getTitle());
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));

        // 소유권 확인
        UserDto userDto;
        try {
            userDto = userClient.getUserId(String.valueOf(userId)).getData().getUser();
        } catch (Exception ex) {
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다: " + userId, ex);
        }
        if (userDto == null || userDto.getUserId() == null) {
            throw new IllegalArgumentException("유효하지 않은 사용자: " + userId);
        }
        if (!review.getUserId().equals(userDto.getUserId())) {
            throw new AccessDeniedException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StarRatingDto> getByAgeGroup(String ageGroup) {
        return reviewRepository.findAll().stream()
                .map(review -> {
                    UserDto user = userClient.getUserId(review.getUserId()).getData().getUser();
                    System.out.println("[DEBUG] StarRatingDto 생성: " + review.getUserId() + " / " + review.getRating());
                    int age = user.getAge();  // 예: 25
                    String group = getAgeGroup(age); // "20대"

                    if (group.equals(ageGroup)) {
                        return new StarRatingDto(review.getMovieId(), review.getRating(), review.getUserId());
                    } else {
                        return null; // 필터 대상 아님
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StarRatingDto> getByGender(String gender) {
        return reviewRepository.findAll().stream()
                .map(review -> {
                    UserDto user = userClient.getUserId(review.getUserId()).getData().getUser();
                    System.out.println("[DEBUG] StarRatingDto 생성: " + review.getUserId() + " / " + review.getRating());
                    String userGender = user.getGender();

                    if (gender.equalsIgnoreCase(userGender)) {
                        return new StarRatingDto(review.getMovieId(), review.getRating(), review.getUserId());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<StarRatingDto> getByAgeAndGender(String ageGroup, String gender) {
        return reviewRepository.findAll().stream()
                .map(review -> {
                    UserDto user = userClient.getUserId(review.getUserId()).getData().getUser();
                    System.out.println("[DEBUG] StarRatingDto 생성: " + review.getUserId() + " / " + review.getRating());
                    int age = user.getAge();
                    String userAgeGroup = getAgeGroup(age);
                    String userGender = user.getGender();

                    if (userAgeGroup.equals(ageGroup) && gender.equalsIgnoreCase(userGender)) {
                        return new StarRatingDto(review.getMovieId(), review.getRating(),  review.getUserId());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private String getAgeGroup(Integer age) {
        if (age == null) return "기타";
        if (age < 10) return "10대 미만";
        if (age < 20) return "10대";
        if (age < 30) return "20대";
        if (age < 40) return "30대";
        if (age < 50) return "40대";
        if (age < 60) return "50대";
        if (age < 70) return "60대";
        return "70대 이상";
    }

    @Override
    public List<Double> getRatingsByMovieId(Long movieId) {
        return reviewRepository.findRatingsByMovieId(movieId);
    }



}
