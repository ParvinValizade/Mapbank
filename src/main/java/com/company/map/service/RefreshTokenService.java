package com.company.map.service;

import com.company.map.dto.TokenRefreshResponse;
import com.company.map.exception.TokenIsExpiredException;
import com.company.map.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    private final JwtProvider jwtProvider;

    public RefreshTokenService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String createRefreshToken(String userName) {
        return jwtProvider.generateRefreshToken(userName);
    }

    public TokenRefreshResponse generateNewAccessToken(String token){
        try {
            Claims claims = jwtProvider.getRefreshTokenClaims(token);

            return new TokenRefreshResponse(jwtProvider.generateAccessToken(claims.getSubject()),
                    jwtProvider.generateRefreshToken(claims.getSubject()));
        } catch (ExpiredJwtException e){
         throw new TokenIsExpiredException("Refresh token is expired..");
        }
    }
}
