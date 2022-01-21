package com.detection.maize.disease.community;

import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.IssueModel;
import com.detection.maize.disease.community.payload.AnswerRequest;
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
    public ResponseEntity<IssueModel> postAnIssue(@RequestParam(name = "image", required = false) MultipartFile image, @RequestParam(name = "issue", required = true) String issueModel, @PathVariable("userUuid") String userUuid) {
        return communityService.createIssue(image, issueModel, userUuid);
    }

    @Transactional
    @GetMapping("/{issueUuid}")
    public ResponseEntity<byte[]> getIssueImageUrl(@PathVariable String issueUuid) {
        return communityService.downloadImage(issueUuid);
    }

    @Transactional
    @GetMapping("/issues")
    public ResponseEntity<PagedModel<?>> getIssues(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<IssueEntity> pagedResourceAssembler) {
        return communityService.getPagedIssueModels(page, size, pagedResourceAssembler);
    }


    @Transactional
    @GetMapping("/answers/{issueUuid}")
    public ResponseEntity<PagedModel<?>> getAnswers(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<AnswerEntity> pagedResourceAssembler,
            @PathVariable("issueUuid") String issueUuid) {
        return communityService.getIssueAnswers(issueUuid, page, size, pagedResourceAssembler);
    }

    @Transactional
    @PostMapping("/issues/answer/{issueUuid}/{userUuid}")
    public ResponseEntity<PagedModel<?>> answerAnIssue(
            @PathVariable("issueUuid") String issueUuid,
            @PathVariable("userUuid") String userUuid,
            @Valid @RequestBody AnswerRequest answerRequest,
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        return communityService.answerIssue(issueUuid, userUuid, answerRequest, page, size, pagedResourcesAssembler);
    }

    @PutMapping("/issues/up-vote/{issueUuid}/{userUuid}")
    @Transactional
    public ResponseEntity<IssueModel> upVoteIssue(@PathVariable("issueUuid") String issueUuid, @PathVariable("userUuid") String userUuid) {
        return communityService.upVoteAnIssue(issueUuid, userUuid);
    }

    @PutMapping("/issues/down-vote/{issueUuid}/{userUuid}")
    @Transactional
    public ResponseEntity<IssueModel> downVoteIssue(@PathVariable("issueUuid") String issueUuid, @PathVariable("userUuid") String userUuid) {
        return communityService.downVoteAnIssue(issueUuid, userUuid);
    }

    @PutMapping("/issues/answers/like-answer/{issueUuid}/{answerUuid}/{userUuid}")
    @Transactional
    public ResponseEntity<PagedModel<?>> likeAnswer(@PathVariable("issueUuid") String issueUuid,
                                                    @PathVariable("answerUuid") String answerUuid,
                                                    @PathVariable("userUuid") String userUuid,
                                                    PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        return communityService.likeIssueAnswer(issueUuid, answerUuid, userUuid, pagedResourcesAssembler);
    }

    @PutMapping("/issues/answers/dislike-answer/{issueUuid}/{answerUuid}/{userUuid}")
    @Transactional
    public ResponseEntity<PagedModel<?>> disLikeAnswer(@PathVariable("issueUuid") String issueUuid,
                                                    @PathVariable("answerUuid") String answerUuid,
                                                    @PathVariable("userUuid") String userUuid,
                                                    PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        return communityService.dislikeIssueAnswer(issueUuid, answerUuid, userUuid, pagedResourcesAssembler);
    }

}
