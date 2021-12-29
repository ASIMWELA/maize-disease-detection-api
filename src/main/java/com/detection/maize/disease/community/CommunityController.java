package com.detection.maize.disease.community;

import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.IssueModel;
import com.detection.maize.disease.community.payload.AnswerRequest;
import com.detection.maize.disease.community.payload.IssueAnswersDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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

    @Transactional
    @GetMapping("/issues")
    public ResponseEntity<PagedModel<IssueModel>> getIssues(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "10")int size,
            PagedResourcesAssembler<IssueEntity> pagedResourceAssembler){
        return communityService.getPagedIssueModels(page, size, pagedResourceAssembler);
    }
    //TODO : test the end point: for next day
    @Transactional
    @PostMapping("/issues/answer/{issueUuid}/{userUuid}")
    public ResponseEntity<IssueAnswersDto> answerAnIssue(
            @PathVariable("issueUuid") String issueUuid,
            @PathVariable("userUuid") String userUuid,
            @Valid AnswerRequest answerRequest
            ){
        return communityService.answerIssue(issueUuid, userUuid, answerRequest);
    }

}
