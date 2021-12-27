package com.detection.maize.disease.community;

import com.detection.maize.disease.community.hateos.IssueModel;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/community")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommunityController {
    CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Transactional
    @PostMapping("/post-issue/{userUuid}")
    public ResponseEntity<IssueModel> postAnIssue(@RequestParam(name="image", required = false) MultipartFile image, @RequestParam(name="issue", required = true)String issueModel, @PathVariable("userUuid") String userUuid){
        return communityService.createIssue(image, issueModel, userUuid);
    }

    @Transactional
    @GetMapping("/{issueUuid}")
    public ResponseEntity<byte[]> getIssueImageUrl(@PathVariable String issueUuid){
        return communityService.downloadImage(issueUuid);
    }

}
