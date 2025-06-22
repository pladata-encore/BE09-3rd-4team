package com.smile.recommendservice.service;

import com.smile.recommendservice.common.ApiResponse;
import com.smile.recommendservice.domain.dto.RecommendationResultDto;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import com.smile.recommendservice.domain.service.RecommendationPolicy;
import com.smile.recommendservice.domain.type.RecommendationType;
import com.smile.recommendservice.dto.MovieDto;
import com.smile.recommendservice.dto.StarRatingDto;
import com.smile.recommendservice.dto.UserDto;
import com.smile.recommendservice.repository.MovieClient;
import com.smile.recommendservice.repository.ReviewClient;
import com.smile.recommendservice.repository.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// 성별 기반 추천 서비스
@Service
@RequiredArgsConstructor
public class GenderBasedRecommendationService implements RecommendationPolicy {

    private final ReviewClient reviewClient;
    private final MovieClient movieClient;

    public RecommendationResultDto recommend(UserDetailsWrapper userDetailsWrapper) {

        // 1. 현재 로그인한 사용자 정보 가져오기
        UserDto user = userDetailsWrapper.getUser();
        String gender = user.getGender(); // "남성" 또는 "여성"

        // ----------------------------------------------------
        System.out.println("[DEBUG] 받은 gender: " + gender);

        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("gender 값이 유효하지 않습니다.");
        }
        // ----------------------------------------------------


        // 2. 같은 성별의 사용자들이 남긴 별점 정보 가져오기
        List<StarRatingDto> ratings = reviewClient.getByGender(gender);



        // 3. 본인의 평점은 제외
        ratings = ratings.stream()
                .filter(r -> r.getRating() != null) // null 제거
                .filter(r -> !r.getUserId().equals(user.getUserId()))
                .collect(Collectors.toList());



        // 4. 영화별 평균 평점 계산
        Map<Long, Double> movieAvgRatings = ratings.stream()
                .collect(Collectors.groupingBy(
                        StarRatingDto::getMovieId,
                        Collectors.averagingDouble(StarRatingDto::getRating)
                ));

        // 5. 평균 평점 높은 순으로 Top 10 영화 ID 추출
        List<Long> topMovieIds = movieAvgRatings.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 6. 영화 ID를 기반으로 상세 영화 정보 조회
        List<MovieDto> movieDtoList = topMovieIds.stream()
                .map(movieId -> movieClient.getMovieById(movieId).getData())
                .collect(Collectors.toList());

        System.out.println("[DEBUG] 받은 rating size: " + ratings.size());
        for (StarRatingDto r : ratings) {
            System.out.println("[DEBUG] rating: " + r.getRating() + ", movieId: " + r.getMovieId() + ", userId: " + r.getUserId());
        }


        // 7. 추천 결과 반환
        return RecommendationResultDto.builder()
                .recommendationType(RecommendationType.GENDER_BASED)
                .criteria(gender)
                .generatedAt(LocalDateTime.now())
                .movies(movieDtoList)
                .build();
    }
}
