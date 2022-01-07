package com.detection.maize.disease.community.impl;

import com.detection.maize.disease.commons.Constants;
import com.detection.maize.disease.community.CommunityController;
import com.detection.maize.disease.community.CommunityService;
import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.hateos.AnswerModel;
import com.detection.maize.disease.community.hateos.AnswerModelAssembler;
import com.detection.maize.disease.community.hateos.IssueModel;
import com.detection.maize.disease.community.hateos.IssueModelAssembler;
import com.detection.maize.disease.community.payload.AnswerRequest;
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
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Slf4j
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
    public ResponseEntity<PagedModel<?>> getPagedIssueModels(int page, int size, PagedResourcesAssembler<IssueEntity> pagedResourcesAssembler) {
        Page<IssueEntity> issues = issueRepository.findAll(PageRequest.of(page, size));
        if (issues.hasContent()) {
            return ResponseEntity.ok(pagedResourcesAssembler
                    .toModel(issues, issueModelAssembler));
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toEmptyModel(issues, IssueModel.class));
    }

    @Override
    public ResponseEntity<PagedModel<?>> answerIssue(String issueUuid, String userUuid, AnswerRequest answerRequest, int page, int size, PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue with the given id")
        );
        UserEntity user = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given entity")
        );
        if (issue.getUser().getEmail().equals(user.getEmail())) {
            throw new OperationNotAllowedException("You cannot answer to your own created issue");
        }
        AnswerEntity answer = AnswerEntity.builder()
                .answerContent(answerRequest.getAnswer())
                .createdAt(LocalDate.now())
                .modifiedAt(LocalDate.now())
                .uuid(UuidGenerator.generateRandomString(12))
                .user(user)
                .issue(issue)
                .build();
        answerRepository.save(answer);
        return this.getIssueAnswers(issue.getUuid(), page, size, pagedResourcesAssembler);
    }

    @Override
    public ResponseEntity<PagedModel<?>> getIssueAnswers(String issueUuid, int page, int size, PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue with  the provided id")
        );
        Page<AnswerEntity> answers = answerRepository.findByIssue(issue, PageRequest.of(page, size));
        if (answers.hasContent()) {
            PagedModel<AnswerModel> answerModels = pagedResourcesAssembler.toModel(answers, answerModelAssembler);
            answerModels.add(linkTo(methodOn(CommunityController.class).getIssues(Constants.PAGE, Constants.SIZE, null)).withRel("issues"));
            return ResponseEntity.ok(answerModels);
        }
        PagedModel<?> objects = pagedResourcesAssembler.toEmptyModel(answers, AnswerModel.class);
        objects.add(linkTo(methodOn(CommunityController.class).getIssues(Constants.PAGE, Constants.SIZE, null)).withRel("issues"));
        return ResponseEntity.ok(objects);
    }

    @Override
    public ResponseEntity<IssueModel> upVoteAnIssue(String issueUuid, String userUuid) {

        IssueEntity issueEntity = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue found with the given identifier")
        );
        UserEntity userEntity = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user found with the given identifier")
        );
        if (issueEntity.getUser().getUuid().equals(userEntity.getUuid())) {
            throw new OperationNotAllowedException("You cannot vote on your own issue");
        }
        List<UserEntity> issueDislikes = issueEntity.getIssueDislikes();

        List<UserEntity> issueVoters = issueEntity.getIssueVotes();
        issueVoters.forEach(user -> {
            if (user.getUuid().equals(userUuid)) {
                throw new OperationNotAllowedException("You have already voted on this issue");
            }
        });
        log.info(String.valueOf(issueDislikes.size()));
        //if the user disliked the issue, remove him/her
        issueDislikes.forEach(userEntity1 -> {
            if(userEntity1.getUuid().equals(userEntity.getUuid())){
                issueDislikes.remove(userEntity);
                log.info(String.valueOf(issueDislikes.size()));
                issueEntity.setIssueDislikes(issueDislikes);
                issueRepository.save(issueEntity);
            }
        });

        if (issueVoters.isEmpty()) {
            issueEntity.setIssueVotes(Stream.of(userEntity).collect(Collectors.toList()));
            issueRepository.save(issueEntity);
            return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
        }

        issueVoters.add(userEntity);
        issueEntity.setIssueVotes(issueVoters);
        return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
    }

    @Override
    public ResponseEntity<IssueModel> downVoteAnIssue(String issueUuid, String userUuid) {
        UserEntity user = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given identity")
        );

        IssueEntity issueEntity = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No Issue with the given identity")
        );
        if (issueEntity.getUser().getUuid().equals(user.getUuid())) {
            throw new OperationNotAllowedException("You cannot dislike your own issue");
        }


        //TODO : IMPLEMENT A LIKE AND DISLIKE
        List<UserEntity> issueDislikes = issueEntity.getIssueDislikes();
        List<UserEntity> issueLikes = issueEntity.getIssueVotes();
       // issueEntity.setIssueVotes(new ArrayList<>());

        issueLikes.forEach(userEntity1->{
            if(userEntity1.getUuid().equals(user.getUuid())){
                issueDislikes.remove(user);
                issueEntity.setIssueVotes(issueDislikes);
                issueRepository.save(issueEntity);
            }
        });

        if (issueDislikes.isEmpty()) {
            issueEntity.setIssueDislikes(Stream.of(user).collect(Collectors.toList()));
            issueRepository.save(issueEntity);
            return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
        }
        issueDislikes.forEach(userEntity -> {
            if (userEntity.getUuid().equals(userUuid)) {
                throw new OperationNotAllowedException("You have already voted on this issue");
            }
        });
        issueDislikes.remove(user);
        issueEntity.setIssueDislikes(issueDislikes);

        return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
    }

}
