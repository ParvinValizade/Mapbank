package com.company.map.service;

import com.company.map.dto.CreateUserRequest;
import com.company.map.dto.LoginRequest;
import com.company.map.dto.TokenResponseDto;
import com.company.map.dto.UserDto;
import com.company.map.dto.converter.UserDtoConverter;
import com.company.map.exception.UserAlreadyExistException;
import com.company.map.exception.UserNotFoundException;
import com.company.map.model.User;
import com.company.map.repository.UserRepository;
import com.company.map.security.jwt.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final UserDtoConverter converter;

    private final RefreshTokenService refreshTokenService;

    public UserService(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                       UserRepository userRepository, UserDtoConverter converter,RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.converter = converter;
        this.refreshTokenService = refreshTokenService;
    }

    public UserDto createUser(CreateUserRequest request){
        checkUserAlreadyExistOrNot(request.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );
        return converter.convertToUserDto(userRepository.save(user));
    }

    public UserDto findUserByUserName(String username){
        return converter.convertToUserDto(findUser(username));
    }

    public List<UserDto> getAllUsers(){
        return converter.convertToUserDtoList(userRepository.findAll());
    }

    public TokenResponseDto login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())
        );

        String refreshToken  = refreshTokenService.createRefreshToken(loginRequest.getUsername());
        return new TokenResponseDto(jwtProvider.generateToken(authentication),
                refreshToken,
                findUserByUserName(loginRequest.getUsername()));
    }

    private void checkUserAlreadyExistOrNot(String username){
        userRepository.findByUsername(username)
                .ifPresent(user -> {throw new UserAlreadyExistException("User already exist..");});
    }

    private User findUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new UserNotFoundException("User couldn't be found by following username: "+username));
    }
}
