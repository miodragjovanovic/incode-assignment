package com.name.no.incode.assignment.controller;

import com.name.no.incode.assignment.dto.request.TransformRequest;
import com.name.no.incode.assignment.dto.request.Transformer;
import com.name.no.incode.assignment.dto.response.Transformation;
import com.name.no.incode.assignment.exception.FetchTransformerValidationException;
import com.name.no.incode.assignment.service.TransformerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static com.name.no.incode.assignment.enums.TransformerType.REGEX_REMOVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransformerControllerTest {

    @Mock
    private TransformerService transformerService;

    @InjectMocks
    private TransformerController transformerController;

    @Test
    void testTransform() {
        // GIVEN
        TransformRequest transformRequest = new TransformRequest(
                "test input",
                List.of(new Transformer(REGEX_REMOVE, "testRegex", "testReplacement")));

        String transformedInput = "transformed input";
        when(transformerService.transform(
                anyString(),
                any(),
                anyString(),
                anyString())
        ).thenReturn(transformedInput);
        doNothing().when(transformerService).validateTransformers(anyList());

        // WHEN
        ResponseEntity<String> response = transformerController.transform(transformRequest);

        // THEN
        assertNotNull(response);
        assertEquals(transformedInput, response.getBody());
        verify(transformerService, times(1)).transform(anyString(), any(), anyString(), anyString());
        verify(transformerService, times(1)).validateTransformers(anyList());
    }

    @Test
    void testGetTransformations() {
        // GIVEN
        LocalDateTime from = LocalDateTime.of(2023, 3, 30, 0, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 31, 0, 0, 0, 0);

        Transformation transformation1 = new Transformation();
        Transformation transformation2 = new Transformation();

        List<Transformation> transformations = List.of(transformation1, transformation2);
        when(transformerService.getTransformations(from, to)).thenReturn(transformations);

        // WHEN
        ResponseEntity<List<Transformation>> response = transformerController.getTransformations(from, to);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(transformerService, times(1)).getTransformations(from, to);
    }

    @Test
    void testGetTransformationsWithInvalidDateRange() {
        // GIVEN
        LocalDateTime from = LocalDateTime.of(2023, 3, 30, 0, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2023, 3, 29, 0, 0, 0, 0);

        // WHEN
        FetchTransformerValidationException exception = assertThrows(FetchTransformerValidationException.class,
                () -> transformerController.getTransformations(from, to));

        // THEN
        assertEquals("'from' value must be lesser than 'to' value", exception.getLocalizedMessage());
        verify(transformerService, times(0)).getTransformations(from, to);
    }
}
