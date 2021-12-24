package com.detection.maize.disease.user.impl;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.enums.ERole;
import com.detection.maize.disease.exception.EntityAlreadyExistException;
import com.detection.maize.disease.exception.EntityNotFoundException;
import com.detection.maize.disease.role.Role;
import com.detection.maize.disease.role.RoleRepository;
import com.detection.maize.disease.user.UserService;
import com.detection.maize.disease.user.entity.UserEntity;
import com.detection.maize.disease.user.payload.SignupRequest;
import com.detection.maize.disease.user.repository.UserRepository;
import com.detection.maize.disease.util.UuidGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<ApiResponse> signup(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EntityAlreadyExistException("Email already taken");
        }

        log.info("Signup customer request ");
        UserEntity userEntity = UserEntity.builder()
                .firstName(signupRequest.getFirstName())
                .email(signupRequest.getEmail())
                .isActive(true)
                .lastName(signupRequest.getLastName())
                .password(signupRequest.getPassword())
                .uuid(UuidGenerator.generateRandomString(12))
                .build();
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        Role roleUser = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(
                () -> new EntityNotFoundException("Role not set")
        );
        userEntity.setRoles(Collections.singletonList(roleUser));
        userRepository.save(userEntity);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Signup successful").build());
    }
}
