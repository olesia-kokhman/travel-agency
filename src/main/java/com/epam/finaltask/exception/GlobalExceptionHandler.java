package com.epam.finaltask.exception;

import com.epam.finaltask.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFound(GeneralApiException exception,
                                                               HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        mapFields(apiErrorResponse, exception, request);

        apiErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        apiErrorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        apiErrorResponse.setFieldErrors(null);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException exception,
                                                                     HttpServletRequest request) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        mapFields(apiErrorResponse, exception, request);

        apiErrorResponse.setStatus(HttpStatus.CONFLICT.value());
        apiErrorResponse.setError(HttpStatus.CONFLICT.getReasonPhrase());
        apiErrorResponse.setFieldErrors(null);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException exception,
                                                                            HttpServletRequest request) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setMessage(exception.getMessage());
        apiErrorResponse.setErrorCode("AUTH_BAD_CREDENTIALS");

        apiErrorResponse.setPath(request.getRequestURI());
        apiErrorResponse.setMethod(request.getMethod());

        apiErrorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiErrorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        apiErrorResponse.setFieldErrors(null);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<ApiErrorResponse> handleClassCast(ClassCastException exception, HttpServletRequest request) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setMessage("Internal authentication error");
        apiErrorResponse.setErrorCode("AUTH_INTERNAL");

        apiErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiErrorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        apiErrorResponse.setFieldErrors(null);

        apiErrorResponse.setPath(request.getRequestURI());
        apiErrorResponse.setMethod(request.getMethod());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                         HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setMessage("Validation failed");
        apiErrorResponse.setErrorCode("FIELD_VALIDATION_ERROR");

        apiErrorResponse.setPath(request.getRequestURI());
        apiErrorResponse.setMethod(request.getMethod());

        apiErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        apiErrorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        List<FieldErrorDto> fieldErrorDtos = fieldErrors.stream().map(error -> {
            FieldErrorDto fieldErrorDto = new FieldErrorDto();
            fieldErrorDto.setField(error.getField());
            fieldErrorDto.setMessage(error.getDefaultMessage());
            Object rejected = error.getRejectedValue();
            fieldErrorDto.setRejected(rejected == null ? null : String.valueOf(rejected));

            return fieldErrorDto;
        }).toList();

        List<FieldErrorDto> globalErrorDtos = exception.getBindingResult().getGlobalErrors().stream()
                .map(err -> {
                    FieldErrorDto dto = new FieldErrorDto();
                    dto.setField("global");
                    dto.setMessage(err.getDefaultMessage());
                    dto.setRejected(null);
                    return dto;
                }).toList();

        List<FieldErrorDto> allFieldErrors = new ArrayList<>();
        allFieldErrors.addAll(fieldErrorDtos);
        allFieldErrors.addAll(globalErrorDtos);

        apiErrorResponse.setFieldErrors(allFieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }

    private void mapFields(ApiErrorResponse apiErrorResponse,
                                       GeneralApiException exception,
                                       HttpServletRequest request) {

        apiErrorResponse.setMessage(exception.getMessage());
        apiErrorResponse.setErrorCode(exception.getErrorCode());

        apiErrorResponse.setPath(request.getRequestURI());
        apiErrorResponse.setMethod(request.getMethod());
    }


}
