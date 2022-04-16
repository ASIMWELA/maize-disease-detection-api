package com.detection.maize.disease.community;

import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.IssueModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/v1/community")
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name="Community Controller", description = "Allows users to create issues for a failed detection. " +
        "A user is also allowed to create an issue not related to crops")
public class CommunityController {
    CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Operation(summary = "Creates an issue in the community. The same created issue is returned")
    @Transactional
    @PostMapping("/post-issue/{userUuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IssueModel> postAnIssue(@RequestParam(name = "image", required = false) MultipartFile image, @RequestParam(name = "issue", required = true) String issueModel, @PathVariable("userUuid") String userUuid) {
        return communityService.createIssue(image, issueModel, userUuid);
    }

    @Transactional
    @GetMapping("/issue-image/{issueUuid}")
    @Operation(summary = "Gets an image of a particular issue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getIssueImageUrl(@PathVariable String issueUuid) {
        return communityService.downloadImage(issueUuid);
    }

    @Transactional
    @GetMapping("/answers/answer-image/{answerUuid}")
    @Operation(summary = "Gets an image of a particular answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getAnswerImage(@PathVariable("answerUuid") String answerUuid) {
        return communityService.renderAnswerImage(answerUuid);
    }

    @Transactional
    @GetMapping("/issues")
    @Operation(summary = "Get paged issues")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<?>> getIssues(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<IssueEntity> pagedResourceAssembler) {
        return communityService.getPagedIssueModels(page, size, pagedResourceAssembler);
    }


    @Transactional
    @GetMapping("/answers/{issueUuid}")
    @Operation(summary = "Get paged answers. Produces a paged model os issue answers")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<?>> getAnswers(
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<AnswerEntity> pagedResourceAssembler,
            @PathVariable("issueUuid") String issueUuid) {
        return communityService.getIssueAnswers(issueUuid, page, size, pagedResourceAssembler);
    }

    @Transactional
    @PostMapping("/issues/answer/{issueUuid}/{userUuid}")
    @Operation(summary = "Give an answer to a particular issue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<?>> answerAnIssue(
            @PathVariable("issueUuid") String issueUuid,
            @PathVariable("userUuid") String userUuid,
            @RequestParam(value = "image", required = false) MultipartFile file,
            @RequestParam("answer") String answerConv,
            @PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
            @Positive @RequestParam(value = "size", defaultValue = "20") int size,
            PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        return communityService.answerIssue(issueUuid, userUuid, file,answerConv, page, size, pagedResourcesAssembler);
    }

    @PutMapping("/issues/up-vote/{issueUuid}/{userUuid}")
    @Transactional
    @Operation(summary = "Like an issue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<IssueModel> upVoteIssue(@PathVariable("issueUuid") String issueUuid, @PathVariable("userUuid") String userUuid) {
        return communityService.upVoteAnIssue(issueUuid, userUuid);
    }

    @PutMapping("/issues/down-vote/{issueUuid}/{userUuid}")
    @Transactional
    @Operation(summary = "Dislike an issue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<IssueModel> downVoteIssue(@PathVariable("issueUuid") String issueUuid, @PathVariable("userUuid") String userUuid) {
        return communityService.downVoteAnIssue(issueUuid, userUuid);
    }

    @PutMapping("/issues/answers/like-answer/{issueUuid}/{answerUuid}/{userUuid}")
    @Transactional
    @Operation(summary = "Like an answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<?>> likeAnswer(@PathVariable("issueUuid") String issueUuid,
                                                    @PathVariable("answerUuid") String answerUuid,
                                                    @PathVariable("userUuid") String userUuid,
                                                    PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        return communityService.likeIssueAnswer(issueUuid, answerUuid, userUuid, pagedResourcesAssembler);
    }

    @PutMapping("/issues/answers/dislike-answer/{issueUuid}/{answerUuid}/{userUuid}")
    @Transactional
    @Operation(summary = "Dislike an answer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<?>> disLikeAnswer(@PathVariable("issueUuid") String issueUuid,
                                                    @PathVariable("answerUuid") String answerUuid,
                                                    @PathVariable("userUuid") String userUuid,
                                                    PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        return communityService.dislikeIssueAnswer(issueUuid, answerUuid, userUuid, pagedResourcesAssembler);
    }

    @PutMapping("/issues/resolve-issue/{issueUuid}/{userUuid}")
    @Transactional
    @Operation(summary = "Mark an issue as resolved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<IssueModel> resolve(@PathVariable("issueUuid") String issueUuid,
                                                       @PathVariable("userUuid") String userUuid) {
        return communityService.resolveAnIssue(issueUuid, userUuid);
    }

}
