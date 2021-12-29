package com.detection.maize.disease.community;

import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.IssueModel;
import com.detection.maize.disease.community.payload.AnswerRequest;
import com.detection.maize.disease.community.payload.IssueAnswersDto;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CommunityService {
    ResponseEntity<IssueModel> createIssue(MultipartFile file, String issueModelConv, String userUuid);
    ResponseEntity<byte[]> downloadImage(String issueUuid);
    ResponseEntity<PagedModel<IssueModel>> getPagedIssueModels(int page, int size, PagedResourcesAssembler<IssueEntity> pagedResourcesAssembler);
    ResponseEntity<IssueAnswersDto> answerIssue(String issueUud, String userUuid, AnswerRequest answerRequest);

}