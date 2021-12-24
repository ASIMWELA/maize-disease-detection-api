package com.detection.maize.disease.user;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.user.payload.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse> signup(SignupRequest signupRequest);
}
