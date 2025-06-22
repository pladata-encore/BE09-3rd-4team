package com.smile.recommendservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// 별점 정보 전송용 DTO
/***
 * 별점 정보 받아올 때 사용할 수 있는 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StarRatingDto {
    private Long movieId;
    private String userId;
    private Double rating;

}

