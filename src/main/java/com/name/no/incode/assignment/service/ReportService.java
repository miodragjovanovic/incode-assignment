package com.name.no.incode.assignment.service;

import com.name.no.incode.assignment.enums.ReportType;
import com.name.no.incode.assignment.report.TransformationReport;
import com.name.no.incode.assignment.repository.TransformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final TransformationRepository transformationRepository;

    public String getReports(ReportType reportType) {
        List<TransformationReport> reportData = transformationRepository.findTransformationsStats();

        return switch (reportType) {
            case CSV -> generateCsv(reportData);
            case TXT -> generatePlainText(reportData);
        };
    }

    private String generateCsv(List<TransformationReport> reports) {
        log.info("Generating csv report");
        StringBuilder sb = new StringBuilder();
        sb.append("Transformer Type, Transformations, Day\n");

        for (TransformationReport report : reports) {
            sb.append(report.getTransformerType())
                    .append(",")
                    .append(report.getTransformations())
                    .append(",")
                    .append(report.getDay())
                    .append("\n");
        }

        return sb.toString();
    }

    private String generatePlainText(List<TransformationReport> reports) {
        log.info("Generating txt report");
        StringBuilder sb = new StringBuilder();

        for (TransformationReport report : reports) {
            sb.append("Transformer Type: ")
                    .append(report.getTransformerType())
                    .append("\nTransformations: ")
                    .append(report.getTransformations())
                    .append("\nDay: ")
                    .append(report.getDay())
                    .append("\n\n");
        }

        return sb.toString();
    }
}
