package com.smile.gatewayservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class GatewayJwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }

    public String getIdFromJWT(String token) {
        return getClaims(token).get("id", String.class);
    }

    public String getRoleFromJWT(String token) {
        return getClaims(token).get("role", String.class);
    }

    public String getGenderFromJWT(String token) {
        return getClaims(token).get("gender", String.class);
    }

    public Integer getAgeFromJWT(String token) {
        return getClaims(token).get("age", Integer.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
