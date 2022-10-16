package com.company.map.service;

import com.company.map.dto.TokenRefreshResponse;
import com.company.map.exception.RefreshTokenNotFoundException;
import com.company.map.exception.TokenRefreshException;
import com.company.map.model.RefreshToken;
import com.company.map.repository.RefreshTokenRepository;
import com.company.map.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${authentication.jwt.refreshToken-expiration-in-ms}")
    private Long refreshTokenDurationMs;


    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserService userService, JwtProvider jwtProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(()-> new RefreshTokenNotFoundException("Refresh token couldn't be found by following token: "+
                        token));
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken(
                userService.findUserById(userId),
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public void verifyExpiration(RefreshToken token) {
        String refreshToken = token.getToken();
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(refreshToken, "Refresh token was expired. Please make a new signin request");
        }
    }

    public TokenRefreshResponse generateNewAccessToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()-> new RefreshTokenNotFoundException("Refresh token couldn't be found by following token: "+
                        token));
        verifyExpiration(refreshToken);

        RefreshToken newRefreshToken = createRefreshToken(refreshToken.getUser().getId());
        refreshTokenRepository.delete(refreshToken);

        return new TokenRefreshResponse(jwtProvider.token(newRefreshToken.getUser().getUsername()),
                newRefreshToken.getToken());

    }
}
