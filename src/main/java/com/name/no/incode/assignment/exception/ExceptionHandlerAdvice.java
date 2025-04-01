package com.name.no.incode.assignment.exception;

import com.name.no.incode.assignment.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({FetchTransformerValidationException.class})
    public ResponseEntity<ErrorResponse> handleFetchTransformerValidationException(FetchTransformerValidationException e) {
        ErrorResponse responseDto = generateErrorResponseAndLogError(e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TransformerValidationException.class})
    public ResponseEntity<ErrorResponse> handleTransformerValidationException(TransformerValidationException e) {
        ErrorResponse responseDto = generateErrorResponseAndLogError(e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse responseDto = generateErrorResponseAndLogError(e.getMessage());
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    ErrorResponse generateErrorResponseAndLogError(String message) {
        log.error(message);
        return new ErrorResponse(message);
    }

}
