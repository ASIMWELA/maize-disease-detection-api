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
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Augustine Simwela
 * <p>
 * AuthService implementation
 * <p>
 * implements the authentication and authorization logic of the application
 * </p>
 * implements {@link AuthService} interface
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    /**
     * Manages authentication sessions
     */
    AuthenticationManager authenticationManager;

    /**
     * Utility class for generating and validating tokens
     */
    JwtTokenProvider tokenProvider;
    /**
     * DAO layer for interacting with the database
     */
    UserRepository userRepository;

    /**
     * @param authenticationManager  facilitates the flow of jwt. See {@link AuthenticationManager}
     * 
     * @param tokenProvider Utility for generating th token. See {@link JwtTokenProvider}
     * 
     * @param userRepository An abstraction for communication with database. See {@link UserRepository}
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    /**
     * constructor
     *
     * @param loginRequest .Contains username and password for a
     *  user who is attempting to authenticate

     * @return loggedIn user of {@link LoginResponse} containing token and user details

     * @see TokenPayload

     * @see UserModel
     */
    @Override
    @Transactional
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
