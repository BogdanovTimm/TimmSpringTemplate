package com.example.hibernatetest2.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.DisabledException;
// import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auth0.jwt.exceptions.JWTDecodeException;

import java.net.http.HttpResponse;
import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

/**
 * Custom Exception Handler for everything
 */
@RestControllerAdvice
@Slf4j
public class CustomHttpResponseForExceptions extends ResponseEntityExceptionHandler
                                             implements
                                             ErrorController { //? If user goes to the webpage that doesn't exist, he will not have a white page. Also, you need to do something [application-dev.yaml] for it to work. (and, we can create our custom page for that) --v

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode statusCode,
                                                             WebRequest request) {
        log.error(exception.getMessage());
        return ResponseEntity.status(statusCode)
                             .body(exception.toString() +
                                   body.toString() +
                                   headers.toString() +
                                   statusCode.toString() +
                                   request.toString());
    }

    /**
     * Handles Exceptions for every given to functions argument that has Valid annotation above them
     * and that has NotEmpty, Email, etc annotations above the variables to which they will be linked
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode statusCode,
                                                                  WebRequest request) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest()
                             .body(exception.toString() +
                                   headers.toSingleValueMap() +
                                   statusCode.toString() +
                                   request.toString());
    }

    /**
     * Handles Exception when SQL-Command that violates the constraints in some table is called
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest()
                             .body(exception.toString());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> badCredentialsException(BadCredentialsException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage() + ", Incorrect email or password");
    }

    /**
     * For Custom Exception
     */
    /* 
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> apiException(ApiException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                                    HttpResponse.builder()
                                                .timeStamp(now().toString())
                                                .reason(exception.getMessage())
                                                .developerMessage(exception.getMessage())
                                                .status(BAD_REQUEST)
                                                .statusCode(BAD_REQUEST.value())
                                                .build(),
                                    BAD_REQUEST);
    }
    */

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body("Access denied. You don\'t have access" + exception.getMessage());
    }

    /**
     * Exception Handler for All other Exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> allOtherExceptions(Exception exception) {
        log.error(exception.getMessage());
        return ResponseEntity.internalServerError()
                             .body(exception.toString());
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<String> exception(JWTDecodeException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.internalServerError().body("Could not decode the token" + exception.getMessage());
    }

    /**
     * Exception when there is no such row in a table
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest()
                             .body(exception.toString());
    }

    /* *
     * When given text can't be parsed as a number
     */
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> numberFormatexception(NumberFormatException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest()
                             .body(exception.toString());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> disabledException(DisabledException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage() + "User account is currently disabled");
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<String> lockedException(LockedException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage() + "User account is currently locked");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> dataAccessException(DataAccessException exception) {
        log.error(exception.getMessage());
        return ResponseEntity.badRequest()
                             .body(processErrorMessage(exception.getMessage()));
    }

    private String processErrorMessage(String errorMessage) {
        if (errorMessage != null) {
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("AccountVerifications")) {
                return "You already verified your account.";
            }
            if (errorMessage.contains("Duplicate entry") && errorMessage.contains("ResetPasswordVerifications")) {
                return "We already sent you an email to reset your password.";
            }
            if (errorMessage.contains("Duplicate entry")) {
                return "Duplicate entry. Please try again.";
            }
        }
        return "Some error occurred";
    }
}
