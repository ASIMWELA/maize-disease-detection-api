package com.detection.maize.disease.community.impl;

import com.detection.maize.disease.community.SaveImageToFileSystem;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public class SaveImageToFileSysImpl implements SaveImageToFileSystem {
    @Override
    public ResponseEntity<?> upload(MultipartFile imageFile, String userUuid, String issueUuid, HttpServletRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> renderIssueImage(HttpServletRequest request, String imageName, String issueUuid) {
        return null;
    }

    @Override
    public ResponseEntity<Resource> renderAnswerImage(HttpServletRequest request, String imageName, String answerUuid) {
        return null;
    }

}
