package com.name.no.incode.assignment.dto.request;

import com.name.no.incode.assignment.enums.TransformerType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Transformer {

    @NotNull
    private TransformerType transformerType;
    private String regex;
    private String replacement;
}
