package com.name.no.incode.assignment.dto.response;

import com.name.no.incode.assignment.enums.TransformerType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Transformation {

    private String input;
    private String transformedInput;
    private TransformerType transformerType;
    private String regex;
    private String replacement;
    private LocalDateTime createdAt;
}