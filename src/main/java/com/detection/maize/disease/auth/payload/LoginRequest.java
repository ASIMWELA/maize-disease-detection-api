package com.detection.maize.disease.auth.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

/**
 * @author AUgustine
 *
 * Holds values that a users sends when he/she is trying to login into the application
 *
 * The users has to send email and paword and both should not be empty
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotEmpty(message = "email cannot be empty")
    String email;
    @NotEmpty(message = "password cannot be empty")
    String password;
}
