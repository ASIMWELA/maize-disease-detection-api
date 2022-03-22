package com.detection.maize.disease.community.impl;

import com.detection.maize.disease.commons.Constants;
import com.detection.maize.disease.community.CommunityController;
import com.detection.maize.disease.community.CommunityService;
import com.detection.maize.disease.community.entity.AnswerEntity;
import com.detection.maize.disease.community.entity.IssueEntity;
import com.detection.maize.disease.community.enums.EIssueStatus;
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
import org.apache.commons.io.FilenameUtils;
import org.datavec.image.format.ImageInputFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
                    .createdAt(new Date())
                    .modifiedAt(new Date())
                    .issueStatus(EIssueStatus.OPEN)
                    .crop(issueModelConv1.getCrop())
                    .uuid(UuidGenerator.generateRandomString(12))
                    .user(userEntity)
                    .build();
        } else {

            String imageExtension = FilenameUtils.getExtension(file.getOriginalFilename());

            if (!Arrays.asList(Constants.ALLOWED_IMAGES_FOR_SAVING).contains(imageExtension)) {
                throw new OperationNotAllowedException("The image format is not allowed \n allowed formats[jpg, png, bmp, gif, jpeg]");
            }

            if (ImageIO.read(file.getInputStream()) == null) {
                throw new OperationNotAllowedException("The file is not an image");
            }
            issueEntity = IssueEntity
                    .builder()
                    .questionDescription(issueModelConv1.getQuestionDescription())
                    .question(issueModelConv1.getQuestion())
                    .createdAt(new Date())
                    .modifiedAt(new Date())
                    .issueStatus(EIssueStatus.OPEN)
                    .issueImage(file.getBytes())
                    .crop(issueModelConv1.getCrop())
                    .imageName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())))
                    .imageType(file.getContentType())
                    .uuid(UuidGenerator.generateRandomString(12))
                    .user(userEntity)
                    .build();
        }
        log.info("Saved issue " + issueEntity.getUuid());
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
    public ResponseEntity<byte[]> renderAnswerImage(String answeruuid) {
        AnswerEntity answerEntity = answerRepository.findByUuid(answeruuid).orElseThrow(
                () -> new RuntimeException("No image with the provided uuid")
        );

        MediaType contentType = MediaType.parseMediaType(answerEntity.getAnswerImageType());

        return ResponseEntity.ok()
                .contentType(contentType)
                //for download
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+ resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + answerEntity.getAnswerImageName())
                .body(answerEntity.getAnswerImage());
    }

    @Override
    public ResponseEntity<PagedModel<?>> getPagedIssueModels(int page, int size, PagedResourcesAssembler<IssueEntity> pagedResourcesAssembler) {
        Page<IssueEntity> issues = issueRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));

        if (issues.hasContent()) {
            return ResponseEntity.ok(pagedResourcesAssembler
                    .toModel(issues, issueModelAssembler));
        }
        log.info("Returned paged issues");
        return ResponseEntity.ok(pagedResourcesAssembler.toEmptyModel(issues, IssueModel.class));
    }

    @Override
    @SneakyThrows
    public ResponseEntity<PagedModel<?>> answerIssue(String issueUuid, String userUuid, MultipartFile image, String answerConv, int page, int size, PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue with the given id")
        );
        UserEntity user = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given entity")
        );
        if (issue.getUser().getEmail().equals(user.getEmail())) {
            throw new OperationNotAllowedException("You cannot answer to your own created issue");
        }

        AnswerEntity answerEntity = null;
        ObjectMapper mapper = new ObjectMapper();
        AnswerRequest answerRequest = mapper.readValue(answerConv, AnswerRequest.class);

        if (image == null) {
            answerEntity = AnswerEntity.builder()
                    .answerContent(answerRequest.getAnswer())
                    .createdAt(new Date())
                    .modifiedAt(new Date())
                    .uuid(UuidGenerator.generateRandomString(12))
                    .user(user)
                    .issue(issue)
                    .build();
        } else {
            String imageExtension = FilenameUtils.getExtension(image.getOriginalFilename());

            if (!Arrays.asList(Constants.ALLOWED_IMAGES_FOR_SAVING).contains(imageExtension)) {
                throw new OperationNotAllowedException("The image format is not allowed \n allowed formats[jpg, png, bmp, gif, jpeg]");
            }

            if (ImageIO.read(image.getInputStream()) == null) {
                throw new OperationNotAllowedException("The file is not an image");
            }
            answerEntity = AnswerEntity.builder()
                    .answerContent(answerRequest.getAnswer())
                    .createdAt(new Date())
                    .modifiedAt(new Date())
                    .answerImage(image.getBytes())
                    .answerImageType(image.getContentType())
                    .answerImageName(StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename())))
                    .uuid(UuidGenerator.generateRandomString(12))
                    .user(user)
                    .issue(issue)
                    .build();
        }
        answerRepository.save(answerEntity);
        log.info("User " + user.getEmail() + "Answer issue " + issue.getUuid());
        return this.getIssueAnswers(issue.getUuid(), page, size, pagedResourcesAssembler);
    }

    @Override
    public ResponseEntity<PagedModel<?>> getIssueAnswers(String issueUuid, int page, int size, PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue with  the provided id")
        );
        Page<AnswerEntity> answers = answerRepository.findByIssue(issue, PageRequest.of(page, size, Sort.by("id").descending()));
        if (answers.hasContent()) {
            PagedModel<AnswerModel> answerModels = pagedResourcesAssembler.toModel(answers, answerModelAssembler);
            answerModels.add(linkTo(methodOn(CommunityController.class).getIssues(Constants.PAGE, Constants.SIZE, null)).withRel("issues"));
            log.info("Returned issue answers");
            return ResponseEntity.ok(answerModels);
        }
        PagedModel<?> objects = pagedResourcesAssembler.toEmptyModel(answers, AnswerModel.class);
        objects.add(linkTo(methodOn(CommunityController.class).getIssues(Constants.PAGE, Constants.SIZE, null)).withRel("issues"));
        log.info("Returned issue answers");
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
        List<UserEntity> issueDislikes = issueEntity.getIssueDownVotes();
        List<UserEntity> issueVoters = issueEntity.getIssueVotes();
        issueVoters.forEach(user -> {
            if (user.getUuid().equals(userUuid)) {
                throw new OperationNotAllowedException("You have already voted on this issue");
            }
        });

        for (int a = 0; a < issueDislikes.size(); a++) {
            if (issueDislikes.get(a).getUuid().equals(userEntity.getUuid())) {
                issueEntity.removeIssueDownVote(issueDislikes.get(a));
            }
        }

        if (issueVoters.isEmpty()) {
            issueEntity.setIssueVotes(Stream.of(userEntity).collect(Collectors.toList()));
            issueRepository.save(issueEntity);
            return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
        }
        issueEntity.addIssueVote(userEntity);
        log.info("Issue " + issueEntity.getUuid() + " got 1 vote");
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

        List<UserEntity> issueDislikes = issueEntity.getIssueDownVotes();
        List<UserEntity> issueLikes = issueEntity.getIssueVotes();
        issueDislikes.forEach(userEntity -> {
            if (userEntity.getUuid().equals(userUuid)) {
                throw new OperationNotAllowedException("You have already down voted on this issue");
            }
        });
        if (issueLikes.size() != 0) {
            for (int a = 0; a < issueLikes.size(); a++) {
                if (issueLikes.get(a).getUuid().equals(user.getUuid())) {
                    issueEntity.removeIssueVote(issueLikes.get(a));
                }
            }

        }
        issueEntity.addIssueDownVote(user);
        log.info("Issue " + issueEntity.getUuid() + " got 1 down vote");
        return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
    }

    @Override
    public ResponseEntity<PagedModel<?>> likeIssueAnswer(String issueUuid, String answerUuid,
                                                         String userUuid,
                                                         PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        AnswerEntity answer = answerRepository.findByUuid(answerUuid).orElseThrow(
                () -> new EntityNotFoundException("Answer not found with the given identifier")
        );

        UserEntity user = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given identifier")
        );
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue found with the given identifier")
        );
        if (answer.getUser().getUuid().equals(user.getUuid())) {
            throw new OperationNotAllowedException("You cannot like your own answer");
        }
        List<UserEntity> answerLikes = answer.getAnswerLikes();
        List<UserEntity> answerDislikes = answer.getAnswerDislikes();

        answerLikes.forEach(user1 -> {
            if (user1.getUuid().equals(user.getUuid())) {
                throw new OperationNotAllowedException("You have already liked this answer");
            }
        });

        for (int a = 0; a < answerDislikes.size(); a++) {
            if (answerDislikes.get(a).getUuid().equals(user.getUuid())) {
                answer.removeAnswerDislike(answerDislikes.get(a));
            }
        }
        answer.addAnswerLike(user);

        answerRepository.save(answer);
        log.info("Answer " + answer.getUuid() + " got 1 like");
        return this.getIssueAnswers(issue.getUuid(), Constants.PAGE, Constants.SIZE, pagedResourcesAssembler);
    }

    @Override
    public ResponseEntity<PagedModel<?>> dislikeIssueAnswer(String issueUuid,
                                                            String answerUuid,
                                                            String userUuid, PagedResourcesAssembler<AnswerEntity> pagedResourcesAssembler) {
        AnswerEntity answer = answerRepository.findByUuid(answerUuid).orElseThrow(
                () -> new EntityNotFoundException("Answer not found with the given identifier")
        );

        UserEntity user = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given identifier")
        );
        IssueEntity issue = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue found with the given identifier")
        );
        if (answer.getUser().getUuid().equals(user.getUuid())) {
            throw new OperationNotAllowedException("You cannot dislike your own answer");
        }
        List<UserEntity> answerLikes = answer.getAnswerLikes();
        List<UserEntity> answerDislikes = answer.getAnswerDislikes();

        answerDislikes.forEach(user1 -> {
            if (user1.getUuid().equals(user.getUuid())) {
                throw new OperationNotAllowedException("You have already disliked this answer");
            }
        });
        for (int a = 0; a < answerLikes.size(); a++) {
            if (answerLikes.get(a).getUuid().equals(user.getUuid())) {
                answer.removeAnswerLike(answerLikes.get(a));
            }
        }
        answer.addAnswerDislike(user);

        answerRepository.save(answer);
        log.info("Answer " + answer.getUuid() + " got disliked");
        return this.getIssueAnswers(issue.getUuid(), Constants.PAGE, Constants.SIZE, pagedResourcesAssembler);
    }

    @Override
    public ResponseEntity<IssueModel> resolveAnIssue(String issueUuid, String userUuid) {

        IssueEntity issueEntity = issueRepository.findByUuid(issueUuid).orElseThrow(
                () -> new EntityNotFoundException("No issue found with the given id")
        );

        UserEntity userEntity = userRepository.findByUuid(userUuid).orElseThrow(
                () -> new EntityNotFoundException("No user with the given id")
        );
        if (!issueEntity.getUser().getUuid().equals(userEntity.getUuid())) {
            throw new OperationNotAllowedException("You cannot mark an issue as resolved that you didnt create");
        }
        issueEntity.setIssueStatus(EIssueStatus.RESOLVED);
        return ResponseEntity.ok(issueModelAssembler.toModel(issueRepository.save(issueEntity)));
    }

}
