package com.smile.recommendservice.domain.dto;

import com.smile.recommendservice.domain.type.RecommendationType;
import com.smile.recommendservice.dto.MovieDto;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

// 추천 결과를 응답할 때 사용하는 DTO
//예: 영화 ID 리스트, 영화 제목, 포스터 등의 정보를 담을 수 있음

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationResultDto {

    private RecommendationType recommendationType;  // 예: GENDER_BASED, COMBINED 등
    private String criteria;                        // 예: "20대", "남성", "20대 남성", "액션"
    private LocalDateTime generatedAt;              // 생성 시각
    private List<MovieDto> movies;                  // 추천된 영화 리스트
}