package com.detection.maize.disease.auth.payload;

import com.detection.maize.disease.user.hateos.UserModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Augustine
 *
 * The DTO which is returned upon succuccsful login
 *
 * The DTO returns token payload which includes access token,
 * type of the token and its expiration data
 *
 * The DTO also returns user data of the loggen in user
 *
 *
 * @see TokenPayload
 *
 * @see UserModel
 *
 * */
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class LoginResponse {
    TokenPayload tokenPayload;
    UserModel userData;
}
