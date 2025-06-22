package com.smile.userservice.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    private String userId;
    private String userName;
    private Integer age;
    private String gender;
    private String role;
}
