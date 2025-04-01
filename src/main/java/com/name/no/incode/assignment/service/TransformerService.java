package com.name.no.incode.assignment.service;

import com.name.no.incode.assignment.dto.request.Transformer;
import com.name.no.incode.assignment.dto.response.Transformation;
import com.name.no.incode.assignment.enums.TransformerType;
import com.name.no.incode.assignment.exception.TransformerValidationException;
import com.name.no.incode.assignment.model.TransformationEntity;
import com.name.no.incode.assignment.repository.TransformationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@AllArgsConstructor
@Slf4j
public class TransformerService {

    private final TransformationRepository transformationRepository;

    private final ModelMapper modelMapper;

    public String transform(String input, TransformerType type, String regex, String replacement) {
        log.info("Applying {} transformation", type);
        String transformedInput = switch (type) {
            case LOWERCASE -> input.toLowerCase();
            case UPPERCASE -> input.toUpperCase();
            case REGEX_REMOVE -> input.replaceAll(regex, "");
            case REGEX_REPLACE -> input.replaceAll(regex, replacement);
        };

        TransformationEntity transformationEntity = TransformationEntity.builder()
                .transformerType(type)
                .input(input)
                .transformedInput(transformedInput)
                .regex(regex)
                .replacement(replacement)
                .build();
        transformationRepository.save(transformationEntity);

        return transformedInput;
    }

    public List<Transformation> getTransformations(LocalDateTime from, LocalDateTime to) {
        log.info("Fetching transformations");
        List<TransformationEntity> transformationEntities =
                transformationRepository.findAllByCreatedAtGreaterThanAndCreatedAtLessThan(from, to);
        return transformationEntities.stream()
                .map(transformationEntity ->
                        modelMapper.map(transformationEntity, Transformation.class))
                .toList();
    }

    public void validateTransformers(List<Transformer> transformers) {
        log.info("Validating transformations");
        transformers.forEach(transformer -> {
            String regex = transformer.getRegex();
            String replacement = transformer.getReplacement();

            switch (transformer.getTransformerType()) {
                case REGEX_REMOVE -> {
                    if (StringUtils.isEmpty(regex) || !isValidRegex(regex) || replacement != null) {
                        throw new TransformerValidationException("""
                                For transformation REGEX_REMOVE,
                                'regex' is mandatory and must be a valid regular expression,
                                and 'replacement' is invalid parameter
                                """
                        );
                    }
                }
                case REGEX_REPLACE -> {
                    if (StringUtils.isEmpty(regex) || !isValidRegex(regex) || replacement == null) {
                        throw new TransformerValidationException("""
                                For transformation REGEX_REPLACE,
                                'regex' is mandatory and must be a valid regular expression,
                                and 'replacement' is mandatory
                                """
                        );
                    }
                }
            }
        });
    }

    private boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

}
