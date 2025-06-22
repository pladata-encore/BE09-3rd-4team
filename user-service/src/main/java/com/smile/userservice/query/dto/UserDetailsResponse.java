package com.smile.userservice.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailsResponse {
    private UserDTO user;
}
