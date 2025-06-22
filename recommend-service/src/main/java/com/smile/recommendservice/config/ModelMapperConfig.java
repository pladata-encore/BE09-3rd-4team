package com.smile.recommendservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// ModelMapper를 Bean으로 등록해서
// DTO <-> Entity 변환을 쉽게 하기 위한 설정 클래스
// 아직 안 씀

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
