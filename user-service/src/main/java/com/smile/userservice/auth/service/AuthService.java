package com.smile.userservice.auth.service;

import com.smile.userservice.auth.dto.LoginRequest;
import com.smile.userservice.auth.dto.TokenResponse;
import com.smile.userservice.auth.entity.RefreshToken;
import com.smile.userservice.auth.repository.RefreshTokenRepository;
import com.smile.userservice.command.entity.User;
import com.smile.userservice.command.repository.UserRepository;
import com.smile.userservice.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest request) {

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호"));

        if(!passwordEncoder.matches(request.getUserPwd(), user.getUserPwd())) {
            throw new BadCredentialsException("올바르지 않은 아이디 혹은 비밀번호");
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getUserId(),
                user.getRole().name(),
                user.getGender(),
                user.getAge()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRole().name());

        RefreshToken tokenEntity = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .expiryDate(
                        new Date(System.currentTimeMillis()
                                + jwtTokenProvider.getRefreshExpiration())
                )
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse refreshToken(String providedRefreshToken) {

        jwtTokenProvider.validateToken(providedRefreshToken);
        String userId = jwtTokenProvider.getUserIdFromJWT(providedRefreshToken);

        RefreshToken storedToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("해당 유저로 조회되는 refreshToken이 없습니다."));

        if(!storedToken.getToken().equals(providedRefreshToken)) {
            throw new BadCredentialsException("저장된 refreshToken과 일치하지 않습니다.");
        }

        if(storedToken.getExpiryDate().before(new Date())) {
            throw new BadCredentialsException("refreshToken의 유효 시간이 만료되었습니다.");
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadCredentialsException("해당 refreshToken을 위한 유저가 없습니다."));

        String accessToken = jwtTokenProvider.createToken(
                user.getUserId(),
                user.getRole().name(),
                user.getGender(),
                user.getAge()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRole().name());

        RefreshToken tokenEntity = RefreshToken.builder()
                .userId(user.getUserId())
                .token(refreshToken)
                .expiryDate(
                        new Date(System.currentTimeMillis()
                                + jwtTokenProvider.getRefreshExpiration())
                )
                .build();

        refreshTokenRepository.save(tokenEntity);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }


    public void logout(String refreshToken) {

        jwtTokenProvider.validateToken(refreshToken);
        String userId = jwtTokenProvider.getUserIdFromJWT(refreshToken);

        RefreshToken storedToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("해당 유저로 조회되는 refreshToken이 없습니다."));

        if(!storedToken.getToken().equals(refreshToken)) {
            throw new BadCredentialsException("저장된 refreshToken과 일치하지 않습니다.");
        }

        refreshTokenRepository.deleteById(userId);
    }
}
