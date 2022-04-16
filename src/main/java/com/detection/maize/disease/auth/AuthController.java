package com.detection.maize.disease.auth;

import com.detection.maize.disease.auth.payload.LoginRequest;
import com.detection.maize.disease.auth.payload.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated

@Tag(name = "Authentication controller", description = "Allows users to pass their email and pwassord to be authenticated")
public class AuthController {
    AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @Operation(summary = "Authenticate with the API")
    @PostMapping("/login")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
