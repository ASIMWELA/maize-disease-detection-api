package com.detection.maize.disease.auth;

import com.detection.maize.disease.auth.payload.LoginRequest;
import com.detection.maize.disease.auth.payload.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);
}
