package com.name.no.incode.assignment.exception;

import com.name.no.incode.assignment.dto.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest
public class ExceptionHandlerAdviceTest {

    public static final String ERROR = "error";

    @InjectMocks
    @Spy
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Mock
    private FetchTransformerValidationException fetchTransformerValidationException;
    @Mock
    private TransformerValidationException transformerValidationException;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Test
    void testHandleFetchTransformerValidationException() {
        // GIVEN
        ErrorResponse errorResponse = new ErrorResponse(ERROR);
        doReturn(errorResponse)
                .when(exceptionHandlerAdvice)
                .generateErrorResponseAndLogError(anyString());
        when(fetchTransformerValidationException.getMessage()).thenReturn(ERROR);

        // WHEN
        ResponseEntity<ErrorResponse> response =
                exceptionHandlerAdvice.handleFetchTransformerValidationException(fetchTransformerValidationException);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }

    @Test
    void testHandleTransformerValidationException() {
        // GIVEN
        ErrorResponse errorResponse = new ErrorResponse(ERROR);
        doReturn(errorResponse)
                .when(exceptionHandlerAdvice)
                .generateErrorResponseAndLogError(anyString());
        when(transformerValidationException.getMessage()).thenReturn(ERROR);

        // WHEN
        ResponseEntity<ErrorResponse> response =
                exceptionHandlerAdvice.handleTransformerValidationException(transformerValidationException);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        // GIVEN
        ErrorResponse errorResponse = new ErrorResponse(ERROR);
        doReturn(errorResponse)
                .when(exceptionHandlerAdvice)
                .generateErrorResponseAndLogError(anyString());
        when(methodArgumentNotValidException.getMessage()).thenReturn(ERROR);

        // WHEN
        ResponseEntity<ErrorResponse> response =
                exceptionHandlerAdvice.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotNull(response.getStatusCode());
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(errorResponse, response.getBody());
    }
}
