package com.name.no.incode.assignment.controller;

import com.name.no.incode.assignment.enums.ReportType;
import com.name.no.incode.assignment.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @Test
    void testDownloadReportCSV() throws IOException {
        // GIVEN
        String reportContent = "test,csv,content\n1,2,3";
        when(reportService.getReports(ReportType.CSV)).thenReturn(reportContent);

        // WHEN
        ResponseEntity<Resource> response = reportController.downloadReport(ReportType.CSV);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getBody());
        String result = new String(response.getBody().getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(reportContent, result);
        verify(reportService, times(1)).getReports(ReportType.CSV);
    }

    @Test
    void testDownloadReportTXT() throws IOException {
        // GIVEN
        String reportContent = "test text content";
        when(reportService.getReports(ReportType.TXT)).thenReturn(reportContent);

        // WHEN
        ResponseEntity<Resource> response = reportController.downloadReport(ReportType.TXT);

        // THEN
        assertNotNull(response);
        assertNotNull(response.getBody());
        String result = new String(response.getBody().getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(reportContent, result);
        verify(reportService, times(1)).getReports(ReportType.TXT);
    }
}
