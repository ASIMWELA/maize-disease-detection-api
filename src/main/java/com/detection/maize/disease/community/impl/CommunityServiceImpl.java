package com.detection.maize.disease.community.impl;

import com.detection.maize.disease.community.CommunityService;
import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.AnswerModel;
import com.detection.maize.disease.community.hateos.AnswerModelAssembler;
import com.detection.maize.disease.community.hateos.IssueModel;
import com.detection.maize.disease.community.hateos.IssueModelAssembler;
import com.detection.maize.disease.community.payload.AnswerRequest;
import com.detection.maize.disease.community.payload.IssueAnswersDto;
import com.detection.maize.disease.community.payload.IssueModelConv;
import com.detection.maize.disease.community.repository.AnswerRepository;
import com.detection.maize.disease.community.repository.IssueRepository;
import com.detection.maize.disease.exception.EntityNotFoundException;
import com.detection.maize.disease.exception.OperationNotAllowedException;
import com.detection.maize.disease.user.entity.UserEntity;
import com.detection.maize.disease.user.repository.UserRepository;
import com.detection.maize.disease.util.UuidGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommunityServiceImpl implements CommunityService {
    UserRepository userRepository;
    IssueRepository issueRepository;
    IssueModelAssembler issueModelAssembler;
    AnswerRepository answerRepository;
    AnswerModelAssembler answerModelAssembler;

    public CommunityServiceImpl(UserRepository userRepository,
                                IssueRepository issueRepository,
                                IssueModelAssembler issueModelAssembler,
                                AnswerRepository answerRepository,
                                AnswerModelAssembler answerModelAssembler) {
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
        this.issueModelAssembler = issueModelAssembler;
        this.answerRepository = answerRepository;
        this.answerModelAssembler = answerModelAssembler;
    }

    @Override
    @SneakyThrows
    public ResponseEntity<IssueModel> createIssue(MultipartFile file, String issueModelConv, String userUuid) {
        UserEntity userEntity = userRepository.findByUuid(userUuid).orElseThrow(() ->
                new EntityNotFoundException("No User with the identifier provided")
        );
        ObjectMapper mapper = new ObjectMapper();
        IssueModelConv issueModelConv1 = mapper.readValue(issueModelConv, IssueModelConv.class);

        IssueEntity issueEntity = null;

        if (file == null) {
            issueEntity = IssueEntity
                    .builder()
                    .questionDescription(issueModelConv1.getQuestionDescription())
                    .question(issueModelConv1.getQuestion())
                    .createdAt(LocalDate.now())
                    .modifiedAt(LocalDate.now())
                    .crop(issueModelConv1.getCrop())
                    .uuid(UuidGenerator.generateRandomString(12))
                    .user(userEntity)
                    .build();
        } else {
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new OperationNotAllowedException("The file is not an image");
            }
            issueEntity = IssueEntity
                    .builder()
                    .questionDescription(issueModelConv1.getQuestionDescription())
                    .question(issueModelConv1.getQuestion())
                    .createdAt(LocalDate.now())
                    .modifiedAt(LocalDate.now())
                    .issueImage(file.getBytes())
                    .crop(issueModelConv1.getCrop())
                    .imageName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                    .imageType(file.getContentType())
                    .uuid(UuidGenerator.generateRandomString(12))
                    .user(userEntity)
                    .build();
        }
        return new ResponseEntity<>(IssueModel.build(issueRepository.save(issueEntity)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<byte[]> downloadImage(String issueUuid) {

        IssueEntity issueEntity = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new RuntimeException("No image with the provided uuid")
        );

        MediaType contentType = MediaType.parseMediaType(issueEntity.getImageType());

        return ResponseEntity.ok()
                .contentType(contentType)
                //for download
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+ resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + issueEntity.getImageName())
                .body(issueEntity.getIssueImage());
    }

    @Override
    public ResponseEntity<PagedModel<IssueModel>> getPagedIssueModels(int page, int size, PagedResourcesAssembler<IssueEntity> pagedResourcesAssembler) {
        Page<IssueEntity> issues = issueRepository.findAll(PageRequest.of(page, size));
        PagedModel<IssueModel> issueModelPagedModels = pagedResourcesAssembler
                .toModel(issues, issueModelAssembler);
        return ResponseEntity.ok(issueModelPagedModels);
    }

    @Override
    public ResponseEntity<IssueAnswersDto> answerIssue(String issueUuid, String userUuid, AnswerRequest answerRequest) {
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue with the given id")
        );
        UserEntity user = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given entity")
        );
        AnswerEntity answer = AnswerEntity.builder()
                .answerContent(answerRequest.getAnswer())
                .createdAt(LocalDate.now())
                .modifiedAt(LocalDate.now())
                .uuid(UuidGenerator.generateRandomString(12))
                .user(user)
                .issue(issue)
                .build();
        answerRepository.save(answer);
        return ResponseEntity.ok(IssueAnswersDto.builder()
                .issue(IssueModel.build(issue))
                .answers(answerModelAssembler.toCollectionModel(issue.getAnswers()).getContent())
                .build());
    }

}
