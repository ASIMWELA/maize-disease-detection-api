package com.detection.maize.disease.auth.payload;

import com.detection.maize.disease.user.hateos.UserModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
