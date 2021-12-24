package com.detection.maize.disease.auth.impl;

import com.detection.maize.disease.auth.AuthService;
import com.detection.maize.disease.auth.payload.LoginRequest;
import com.detection.maize.disease.auth.payload.LoginResponse;
import com.detection.maize.disease.auth.payload.TokenPayload;
import com.detection.maize.disease.exception.EntityNotFoundException;
import com.detection.maize.disease.security.JwtTokenProvider;
import com.detection.maize.disease.user.entity.UserEntity;
import com.detection.maize.disease.user.hateos.UserModel;
import com.detection.maize.disease.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    JwtTokenProvider tokenProvider;
    UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        log.info("Requesting to authenticate with the api");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateJwtToken(authentication);

        log.info("Getting user information");
        UserEntity user = userRepository.findByEmail(tokenProvider.getUserNameFromToken(jwt).toLowerCase()).orElseThrow(
                () -> new EntityNotFoundException("Wrong credentials")
        );

        TokenPayload tokenPayload = TokenPayload.builder()
                .accessToken(jwt)
                .type("Bearer")
                .expiresIn(tokenProvider.getExpirationMinutes(jwt))
                .build();
        log.info("Returning user information");
        return ResponseEntity.ok(
                LoginResponse.builder().tokenPayload(tokenPayload)
                        .userData(UserModel.buildUserModel(user)).build());
    }
}
