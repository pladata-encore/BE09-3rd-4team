package com.smile.userservice.command.service;

import com.smile.userservice.command.dto.UserCreateRequest;
import com.smile.userservice.command.entity.User;
import com.smile.userservice.command.entity.UserRole;
import com.smile.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserCreateRequest request) {

        User user = modelMapper.map(request, User.class);   // DTO(UserCreateRequest) -> Entity(User)
        user.setEncodedPassword(passwordEncoder.encode(request.getUserPwd()));
        if (user.getRole() == null) {
            user.setDefaultRole(UserRole.USER);
        }
        userRepository.save(user);
    }
}
