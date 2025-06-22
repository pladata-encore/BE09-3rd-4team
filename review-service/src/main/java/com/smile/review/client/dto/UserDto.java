package com.smile.review.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String userId;
    private String userName;
    private Integer age;
    private String gender;
    private String role;

    // 유틸리티: 나이 → 연령대 변환
    public String getAgeGroup() {
        if (age == null) return "기타"; // 안전 장치
        if (age < 10) return "10대 미만";
        if (age < 20) return "10대";
        if (age < 30) return "20대";
        if (age < 40) return "30대";
        if (age < 50) return "40대";
        if (age < 60) return "50대";
        if (age < 70) return "60대";
        return "70대 이상";
    }
}