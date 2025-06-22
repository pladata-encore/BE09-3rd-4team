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

// 연령 + 성별 기반 평점 데이터를 합쳐서 종합 추천

@Service
@RequiredArgsConstructor
public class CombinedRecommendationService implements RecommendationPolicy {

    private final ReviewClient reviewClient;
    private final MovieClient movieClient;

    public RecommendationResultDto recommend(UserDetailsWrapper userDetailsWrapper) {

        // 1. 로그인 사용자 정보 가져오기
        UserDto user = userDetailsWrapper.getUser();

        String ageGroup = user.getAgeGroup(); // 예: "20대"
        String gender = user.getGender();     // 예: "남성"

        // ----------------------------------------------------
        System.out.println("[DEBUG] 받은 gender: " + ageGroup + "// + 받은 gender " + gender);
        // ----------------------------------------------------

        // 2. 같은 연령대 + 성별의 사용자 평점 조회
        List<StarRatingDto> ratings = reviewClient.getByAgeAndGender(ageGroup, gender);

        // 3. 본인의 평점 제외
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

        // 5. 평점 높은 영화 Top 10 선정
        List<Long> topMovieIds = movieAvgRatings.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 6. 영화 상세 정보 조회
        List<MovieDto> movieDtoList = topMovieIds.stream()
                .map(movieId -> movieClient.getMovieById(movieId).getData())
                .collect(Collectors.toList());

        // 7. 추천 결과 반환
        return RecommendationResultDto.builder()
                .recommendationType(RecommendationType.COMBINED)
                .criteria(ageGroup + " " + gender) // 예: "20대 남성"
                .generatedAt(LocalDateTime.now())
                .movies(movieDtoList)
                .build();
    }
}
