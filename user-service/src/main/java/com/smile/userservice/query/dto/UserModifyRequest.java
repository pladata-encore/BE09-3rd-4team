package com.smile.userservice.query.dto;

import com.smile.userservice.command.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequest {

    private Long id;
    private String userId;
    private String userPwd;
    private String userName;
    private Integer age;
    private String gender;
    private UserRole role;
}
