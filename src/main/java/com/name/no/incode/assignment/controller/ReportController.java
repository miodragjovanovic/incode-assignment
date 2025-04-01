package com.name.no.incode.assignment.controller;

import com.name.no.incode.assignment.enums.ReportType;
import com.name.no.incode.assignment.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadReport(@RequestParam("format") ReportType format) {
        log.info("Received download {} request.", format);
        String content = reportService.getReports(format);

        InputStreamResource resource = new InputStreamResource
                (new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));

        String contentType = switch (format) {
            case CSV -> "text/csv";
            case TXT -> "text/plain";
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=transformation_report." + format.name().toLowerCase())
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
