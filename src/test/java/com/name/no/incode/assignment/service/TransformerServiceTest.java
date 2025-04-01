package com.name.no.incode.assignment.service;

import com.name.no.incode.assignment.dto.request.Transformer;
import com.name.no.incode.assignment.dto.response.Transformation;
import com.name.no.incode.assignment.exception.TransformerValidationException;
import com.name.no.incode.assignment.model.TransformationEntity;
import com.name.no.incode.assignment.repository.TransformationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.name.no.incode.assignment.enums.TransformerType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransformerServiceTest {

    public static final String INPUT = "TeSt";
    public static final String INPUT_FOR_REGEX = "TeSt 123";
    public static final String VALID_REGEX = "\\d+";
    public static final String INVALID_REGEX = "(abc";
    public static final String REPLACEMENT = "456";
    @Mock
    private TransformationRepository transformationRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransformerService transformerService;

    @Test
    void testTransformLowercase() {
        // WHEN
        String result = transformerService.transform(INPUT, LOWERCASE, null, null);

        // THEN
        assertEquals("test", result);
        verify(transformationRepository, times(1)).save(any(TransformationEntity.class));
    }

    @Test
    void testTransformUppercase() {
        // WHEN
        String result = transformerService.transform(INPUT, UPPERCASE, null, null);

        // THEN
        assertEquals("TEST", result);
        verify(transformationRepository, times(1)).save(any(TransformationEntity.class));
    }

    @Test
    void testTransformRegexRemove() {
        // WHEN
        String result = transformerService.transform(INPUT_FOR_REGEX, REGEX_REMOVE, VALID_REGEX, null);

        // THEN
        assertEquals("TeSt ", result);
        verify(transformationRepository, times(1)).save(any(TransformationEntity.class));
    }

    @Test
    void testTransformRegexReplace() {
        // WHEN
        String result = transformerService.transform(INPUT_FOR_REGEX, REGEX_REPLACE, VALID_REGEX, REPLACEMENT);

        // THEN
        assertEquals("TeSt 456", result);
        verify(transformationRepository, times(1)).save(any(TransformationEntity.class));
    }

    @Test
    void testGetTransformations() {
        // GIVEN
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now();
        List<TransformationEntity> entities = List.of(new TransformationEntity(), new TransformationEntity());
        when(transformationRepository.findAllByCreatedAtGreaterThanAndCreatedAtLessThan(from, to)).thenReturn(entities);
        when(modelMapper.map(any(TransformationEntity.class), eq(Transformation.class))).thenReturn(new Transformation());

        // WHEN
        List<Transformation> transformations = transformerService.getTransformations(from, to);

        // THEN
        assertNotNull(transformations);
        assertEquals(2, transformations.size());
        verify(transformationRepository, times(1)).findAllByCreatedAtGreaterThanAndCreatedAtLessThan(from, to);
    }

    @Test
    void testValidateTransformersValid() {
        // GIVEN
        Transformer validTransformer = new Transformer(
                REGEX_REPLACE,
                VALID_REGEX,
                REPLACEMENT
        );

        // WHEN & THEN
        assertDoesNotThrow(() -> transformerService.validateTransformers(List.of(validTransformer)));
    }

    @Test
    void testValidateTransformersInvalidRegexRemove() {
        // GIVEN
        Transformer invalidTransformer = new Transformer(
                REGEX_REMOVE,
                "",
                REPLACEMENT
        );

        // WHEN & THEN
        assertThrows(TransformerValidationException.class,
                () -> transformerService.validateTransformers(List.of(invalidTransformer)));
    }

    @Test
    void testValidateTransformersInvalidRegexReplace() {
        // GIVEN
        Transformer invalidTransformer = new Transformer(
                REGEX_REPLACE,
                VALID_REGEX,
                null
        );

        // WHEN & THEN
        assertThrows(TransformerValidationException.class,
                () -> transformerService.validateTransformers(List.of(invalidTransformer)));
    }

    @Test
    void testValidateTransformersInvalidRegex() {
        // GIVEN
        Transformer invalidTransformer = new Transformer(
                REGEX_REPLACE,
                INVALID_REGEX,
                REPLACEMENT
        );

        // WHEN & THEN
        assertThrows(TransformerValidationException.class,
                () -> transformerService.validateTransformers(List.of(invalidTransformer)));
    }
}
