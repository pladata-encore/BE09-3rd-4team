package com.smile.searchservice.client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String id;       // 사용자 ID
    private String name;     // 사용자 이름
    private String email;    // 사용자 이메일

    // 필요에 따라 다른 사용자 정보 추가 가능
}
