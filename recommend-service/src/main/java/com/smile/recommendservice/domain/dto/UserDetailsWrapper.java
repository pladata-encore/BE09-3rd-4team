package com.smile.recommendservice.domain.dto;

import com.smile.recommendservice.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsWrapper {
    private UserDto user;
}