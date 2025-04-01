package com.name.no.incode.assignment.report;

import com.name.no.incode.assignment.enums.TransformerType;
import lombok.Getter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
public class TransformationReport {

    private TransformerType transformerType;
    private long transformations;
    private LocalDate day;

    public TransformationReport(TransformerType transformerType, long transformations, Date day) {
        this.transformerType = transformerType;
        this.transformations = transformations;
        this.day = day.toLocalDate();
    }
}
