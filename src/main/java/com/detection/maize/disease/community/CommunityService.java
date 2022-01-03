package com.detection.maize.disease.community;

import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.IssueModel;
import com.detection.maize.disease.community.payload.AnswerRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CommunityService {
    ResponseEntity<IssueModel> createIssue(MultipartFile file, String issueModelConv, String userUuid);
    ResponseEntity<byte[]> downloadImage(String issueUuid);
    ResponseEntity<PagedModel<?>> getPagedIssueModels(int page, int size, PagedResourcesAssembler<IssueEntity> pagedResourcesAssembler);
    ResponseEntity<PagedModel<?>> answerIssue(String issueUud, String userUuid,
                                                        AnswerRequest answerRequest,
                                                        int page, int size,
                                                        PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler);
    ResponseEntity<PagedModel<?>> getIssueAnswers(String issueUuid, int page, int size, PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler);
}
