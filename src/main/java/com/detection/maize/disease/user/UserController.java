package com.detection.maize.disease.user;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.user.payload.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User Controller",description = "A user sends a request to create an account")
public class UserController {
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary= "Create a user account if the information given is valid")
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignupRequest signupRequest){
        return userService.signup(signupRequest);
    }
}
