package com.smile.userservice.query.mapper;

import com.smile.userservice.command.entity.UserRole;
import com.smile.userservice.query.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDTO findUserById(String userId);

    int updateUserById(Long id, String userId, String userPwd, String userName, Integer age, String gender, UserRole role);

    int deleteUserById(String userId);;
}
