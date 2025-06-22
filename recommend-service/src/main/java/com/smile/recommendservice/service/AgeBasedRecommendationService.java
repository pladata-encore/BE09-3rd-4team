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

@Service
@RequiredArgsConstructor
public class AgeBasedRecommendationService implements RecommendationPolicy {

    private final ReviewClient reviewClient;
    private final MovieClient movieClient;

    @Override
    public RecommendationResultDto recommend(UserDetailsWrapper userWrapper) {

        // 1. 로그인 사용자 정보 가져오기 (user-service에서 /users/me)
        UserDto user = userWrapper.getUser();
        String ageGroup = user.getAgeGroup(); // 나이 -> "20대", "30대" 등 변환

        // ----------------------------------------------------
        System.out.println("[DEBUG] 받은 gender: " + ageGroup);

        if (ageGroup == null || ageGroup.isBlank()) {
            throw new IllegalArgumentException("gender 값이 유효하지 않습니다.");
        }
        // ----------------------------------------------------

        // 2. 같은 연령대의 사용자들의 별점 정보 조회
        List<StarRatingDto> ratings = reviewClient.getByAgeGroup(ageGroup);

        // 3. 본인(userId 동일) 제외
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

        // 5. 평점 높은 영화 Top 10 뽑기
        List<Long> topMovieIds = movieAvgRatings.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // 높은 순
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 6. 각 영화 ID에 대한 상세 정보 조회 (단일 조회 반복)
        List<MovieDto> movieDtoList = topMovieIds.stream()
                .map(movieId -> movieClient.getMovieById(movieId).getData())
                .collect(Collectors.toList());
        // 7. 추천 결과 생성
        return RecommendationResultDto.builder()
                .recommendationType(RecommendationType.AGE_BASED)
                .criteria(ageGroup)
                .generatedAt(LocalDateTime.now())
                .movies(movieDtoList)
                .build();
    }
}