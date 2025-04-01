package com.name.no.incode.assignment.service;

import com.name.no.incode.assignment.report.TransformationReport;
import com.name.no.incode.assignment.repository.TransformationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static com.name.no.incode.assignment.enums.ReportType.CSV;
import static com.name.no.incode.assignment.enums.ReportType.TXT;
import static com.name.no.incode.assignment.enums.TransformerType.REGEX_REMOVE;
import static com.name.no.incode.assignment.enums.TransformerType.UPPERCASE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReportServiceTest {

    public static final String DATE_STRING = "2023-03-31";
    public static final Date DATE = Date.valueOf(DATE_STRING);
    @Mock
    private TransformationRepository transformationRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void testGetReportsCSV() {
        // GIVEN
        List<TransformationReport> reportData = List.of(
                new TransformationReport(REGEX_REMOVE, 10, DATE),
                new TransformationReport(UPPERCASE, 20, DATE)
        );
        when(transformationRepository.findTransformationsStats()).thenReturn(reportData);

        // WHEN
        String result = reportService.getReports(CSV);

        // THEN
        assertNotNull(result);
        assertTrue(result.contains("Transformer Type, Transformations, Day"));
        assertTrue(result.contains("REGEX_REMOVE"));
        assertTrue(result.contains("10"));
        assertTrue(result.contains(DATE_STRING));
        assertTrue(result.contains("UPPERCASE"));
        assertTrue(result.contains("20"));
        assertTrue(result.contains(DATE_STRING));
        verify(transformationRepository, times(1)).findTransformationsStats();
    }

    @Test
    void testGetReportsTXT() {
        // GIVEN
        List<TransformationReport> reportData = List.of(
                new TransformationReport(REGEX_REMOVE, 10, DATE),
                new TransformationReport(UPPERCASE, 20, DATE)
        );
        when(transformationRepository.findTransformationsStats()).thenReturn(reportData);

        // WHEN
        String result = reportService.getReports(TXT);

        // THEN
        assertNotNull(result);
        assertTrue(result.contains("Transformer Type: REGEX_REMOVE"));
        assertTrue(result.contains("Transformations: 10"));
        assertTrue(result.contains("Day: 2023-03-31"));
        assertTrue(result.contains("Transformer Type: UPPERCASE"));
        assertTrue(result.contains("Transformations: 20"));
        assertTrue(result.contains("Day: 2023-03-31"));
        verify(transformationRepository, times(1)).findTransformationsStats();
    }

}
