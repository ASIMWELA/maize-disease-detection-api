package com.detection.maize.disease.community.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IssueModelConv {
    String issueName;
    String issueDescription;
}
