package com.smile.userservice.auth.service;

import com.smile.userservice.auth.model.CustomUser;
import com.smile.userservice.command.entity.User;
import com.smile.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾지 못했습니다."));

        return CustomUser.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userId(user.getUserId())
                .userPwd(user.getUserPwd())
                .age(user.getAge())
                .gender(user.getGender())
                .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
                .build();
    }
}
