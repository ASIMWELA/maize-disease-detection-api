package com.detection.maize.disease.exception;

import org.hibernate.PropertyValueException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiException apiException) {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiException apiError = new ApiException(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartExpt(MultipartException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExistException(EntityAlreadyExistException ex) {
        ApiException apiError = new ApiException(CONFLICT);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<Object> handleValidationErrors(ValidationException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList()).toString();
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(errorList);
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(PropertyValueException.class)
    protected ResponseEntity<Object> handleValidationErrors(PropertyValueException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> emptyClaims(IllegalArgumentException ex) {
        ApiException apiError = new ApiException(UNAUTHORIZED);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);
    }


    //security related exceptions


//    @ExceptionHandler(AuthenticationException.class)
//    protected ResponseEntity<Object> handleAuthError(AuthenticationException ex)
//    {
//        ApiException apiError = new ApiException(UNAUTHORIZED);
//        apiError.setMessage(ex.getMessage());
//        apiError.setCode(apiError.getStatus().value());
//        return buildResponseEntity(apiError);
//    }
//    @ExceptionHandler(InsufficientAuthenticationException.class)
//    protected ResponseEntity<Object> insufficientAuth(InsufficientAuthenticationException ex)
//    {
//        ApiException apiError = new ApiException(UNAUTHORIZED);
//        apiError.setMessage(ex.getMessage());
//        apiError.setCode(apiError.getStatus().value());
//        return buildResponseEntity(apiError);
//    }
//
//    @ExceptionHandler(MalformedJwtException.class)
//    protected ResponseEntity<Object> malformedJwt(MalformedJwtException ex)
//    {
//        ApiException apiError = new ApiException(UNAUTHORIZED);
//        apiError.setMessage(ex.getMessage());
//        apiError.setCode(apiError.getStatus().value());
//        return buildResponseEntity(apiError);
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    protected ResponseEntity<Object> expiredJwt(ExpiredJwtException ex)
//    {
//        ApiException apiError = new ApiException(UNAUTHORIZED);
//        apiError.setMessage(ex.getMessage());
//        apiError.setCode(apiError.getStatus().value());
//        return buildResponseEntity(apiError);
//    }
//
//    @ExceptionHandler(UnsupportedJwtException.class)
//    protected ResponseEntity<Object> unsupportedJwt(UnsupportedJwtException ex)
//    {
//        ApiException apiError = new ApiException(UNAUTHORIZED);
//        apiError.setMessage(ex.getMessage());
//        apiError.setCode(apiError.getStatus().value());
//        return buildResponseEntity(apiError);
//    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> sql(SQLException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage("Sql Exception occurred : " + ex.getMessage().substring(7, ex.getMessage().indexOf("Detail")));
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);

    }

    @ExceptionHandler(OperationNotAllowedException.class)
    protected ResponseEntity<Object> handleOperationNotAllowed(OperationNotAllowedException ex) {
        ApiException apiError = new ApiException(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setCode(apiError.getStatus().value());
        return buildResponseEntity(apiError);

    }


}
