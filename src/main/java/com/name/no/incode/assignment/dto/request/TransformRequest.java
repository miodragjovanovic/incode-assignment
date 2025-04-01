package com.name.no.incode.assignment.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class TransformRequest {

    @NotBlank
    private String input;
    @NotEmpty
    @Valid
    private List<Transformer> transformers;
}
