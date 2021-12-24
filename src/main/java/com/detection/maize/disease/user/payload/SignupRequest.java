package com.detection.maize.disease.user.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class SignupRequest {
    //personal details
    @Size(min = 2, max = 90, message = "First name should be between 2 and 90 in length")
    @NotEmpty(message = "First name cannot be empty")
    String firstName;
    @Size(min = 2, max = 90, message = "lastName must be between 2 and 90")
    @NotEmpty(message = "userName cannot be empty")
    String lastName;
    @Size(min = 2, max = 90, message = "email has to be between 2 and 90 in length")
    @NotEmpty(message = "email cannot be empty")
    @Email(message = "email provided is not valid")
    String email;
    @NonNull
    @Size(min=5, max = 15, message = "password must be between 5 and 15 characters")
    @NotEmpty(message = "password cannot be blank")
    String password;

}
