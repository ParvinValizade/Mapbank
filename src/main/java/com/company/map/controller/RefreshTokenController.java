package com.company.map.controller;

import com.company.map.dto.TokenRefreshResponse;
import com.company.map.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/refresh")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    public RefreshTokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/{token}")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@PathVariable String token){
        return ResponseEntity.ok(refreshTokenService.generateNewAccessToken(token));
    }
}
