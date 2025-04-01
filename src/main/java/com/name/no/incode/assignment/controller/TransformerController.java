package com.name.no.incode.assignment.controller;

import com.name.no.incode.assignment.dto.request.TransformRequest;
import com.name.no.incode.assignment.dto.request.Transformer;
import com.name.no.incode.assignment.dto.response.Transformation;
import com.name.no.incode.assignment.exception.FetchTransformerValidationException;
import com.name.no.incode.assignment.service.TransformerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class TransformerController {

    private final TransformerService transformerService;

    @PostMapping("/transform")
    public ResponseEntity<String> transform(@Valid @RequestBody TransformRequest transformRequest) {
        log.info("Received transform request: {}", transformRequest);
        String transformedInput = transformRequest.getInput();

        transformerService.validateTransformers(transformRequest.getTransformers());

        for (Transformer transformer : transformRequest.getTransformers()) {
            transformedInput = transformerService.transform(
                    transformedInput,
                    transformer.getTransformerType(),
                    transformer.getRegex(),
                    transformer.getReplacement()
            );
        }
        return ResponseEntity.ok(transformedInput);
    }

    @GetMapping("/transformations")
    public ResponseEntity<List<Transformation>> getTransformations(@RequestParam("from") LocalDateTime from,
                                                                   @RequestParam("to") LocalDateTime to) {
        log.info("Received fetch transformations from {} to {} request.", from, to);
        if (from.isAfter(to)) {
            throw new FetchTransformerValidationException("'from' value must be lesser than 'to' value");
        }
        return ResponseEntity.ok(transformerService.getTransformations(from, to));
    }
}
