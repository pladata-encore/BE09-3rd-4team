package com.smile.recommendservice.domain.service;

import com.smile.recommendservice.domain.dto.RecommendationResultDto;
import com.smile.recommendservice.domain.dto.UserDetailsWrapper;
import com.smile.recommendservice.dto.MovieDto;
import com.smile.recommendservice.dto.UserDto;
import java.util.List;

// 추천 정책을 정의하는 전략(Strategy) 패턴 인터페이스
// 다양한 추천 방식(연령대, 성별 등)을 하나의 타입으로 다루기 위해 설계

public interface RecommendationPolicy {
    RecommendationResultDto recommend(UserDetailsWrapper userWrapper);
}


