package com.detection.maize.disease.community;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface SaveImageToFileSystem {
    ResponseEntity<?> upload(MultipartFile imageFile,String userUuid, String issueUuid, HttpServletRequest request);
    ResponseEntity<Resource> renderIssueImage(HttpServletRequest request,String imageName, String issueUuid);
    ResponseEntity<Resource> renderAnswerImage(HttpServletRequest request,String imageName, String answerUuid);
}
