package com.smile.userservice.command.dto;

import com.smile.userservice.command.entity.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequest {

    private final String userId;
    private final String userPwd;
    private final String userName;
    private final Integer age;
    private final String gender;
    private final UserRole role;
}
