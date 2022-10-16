package com.company.map.controller;

import com.company.map.dto.TokenRefreshRequest;
import com.company.map.dto.TokenRefreshResponse;
import com.company.map.model.RefreshToken;
import com.company.map.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/refresh")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    public RefreshTokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/{token}")
    public ResponseEntity<RefreshToken> hello(@PathVariable String token){
        return ResponseEntity.ok(refreshTokenService.findByToken(token));
    }

    @PostMapping("/{token}")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@PathVariable String token){
        System.out.println("Salam olsun");
        return ResponseEntity.ok(refreshTokenService.generateNewAccessToken(token));
    }
}
