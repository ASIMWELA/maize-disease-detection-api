package com.detection.maize.disease.community;

import com.detection.maize.disease.commons.ApiResponse;
import com.detection.maize.disease.community.hateos.IssueModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CommunityService {
    ResponseEntity<IssueModel> createIssue(MultipartFile file, String issueModelConv, String userUuid);
    ResponseEntity<byte[]> downloadImage(String issueUuid);
}
