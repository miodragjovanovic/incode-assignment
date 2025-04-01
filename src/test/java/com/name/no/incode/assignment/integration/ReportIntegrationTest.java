package com.name.no.incode.assignment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.name.no.incode.assignment.model.TransformationEntity;
import com.name.no.incode.assignment.repository.TransformationRepository;
import com.name.no.incode.assignment.service.ReportService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static com.name.no.incode.assignment.enums.TransformerType.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportIntegrationTest extends BaseIntegrationTest {

    public static final String PATH = "/report/download";
    public static final String FORMAT = "format";
    public static final String INPUT = "TeSt";
    public static final String REGEX = "[a-z]";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReportService reportService;

    @Autowired
    private TransformationRepository transformationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DATE = LocalDate.now().toString();

    @BeforeEach
    void setUp() {
        setUpDb();
    }

    @Test
    void testDownloadReportCSV() throws Exception {
        MvcResult mvcResult =
                mockMvc
                        .perform(MockMvcRequestBuilders.get(PATH)
                                .param(FORMAT, "CSV"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertTrue(response.contains("LOWERCASE,1," + DATE));
        assertTrue(response.contains("REGEX_REMOVE,1," + DATE));
        assertTrue(response.contains("REGEX_REPLACE,1," + DATE));
        assertTrue(response.contains("UPPERCASE,1," + DATE));
    }

    @Test
    void testDownloadReportTXT() throws Exception {
        MvcResult mvcResult =
                mockMvc
                        .perform(MockMvcRequestBuilders.get(PATH)
                                .param(FORMAT, "TXT"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertTrue(response.contains("Transformer Type: LOWERCASE\nTransformations: 1\nDay: " + DATE + "\n\n"));
        assertTrue(response.contains("Transformer Type: REGEX_REMOVE\nTransformations: 1\nDay: " + DATE + "\n\n"));
        assertTrue(response.contains("Transformer Type: REGEX_REPLACE\nTransformations: 1\nDay: " + DATE + "\n\n"));
        assertTrue(response.contains("Transformer Type: UPPERCASE\nTransformations: 1\nDay: " + DATE + "\n\n"));
    }

    @Test
    void testDownloadReportMissingFormat() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get(PATH)
                        .param(FORMAT, ""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    void testDownloadReportUnsupportedFormat() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.get(PATH)
                        .param(FORMAT, "XLSX"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @AfterEach
    void cleanUp() {
        transformationRepository.deleteAll();
    }

    private void setUpDb() {
        TransformationEntity transformationEntity = new TransformationEntity();
        transformationEntity.setInput(INPUT);
        transformationEntity.setTransformerType(UPPERCASE);
        transformationEntity.setRegex(null);
        transformationEntity.setReplacement(null);
        transformationEntity.setTransformedInput("TEST");
        transformationRepository.save(transformationEntity);

        transformationEntity = new TransformationEntity();
        transformationEntity.setInput(INPUT);
        transformationEntity.setTransformerType(LOWERCASE);
        transformationEntity.setRegex(null);
        transformationEntity.setReplacement(null);
        transformationEntity.setTransformedInput("test");
        transformationRepository.save(transformationEntity);

        transformationEntity = new TransformationEntity();
        transformationEntity.setInput(INPUT);
        transformationEntity.setTransformerType(REGEX_REMOVE);
        transformationEntity.setRegex(REGEX);
        transformationEntity.setReplacement(null);
        transformationEntity.setTransformedInput("TS");
        transformationRepository.save(transformationEntity);

        transformationEntity = new TransformationEntity();
        transformationEntity.setInput(INPUT);
        transformationEntity.setTransformerType(REGEX_REPLACE);
        transformationEntity.setRegex(REGEX);
        transformationEntity.setReplacement("1");
        transformationEntity.setTransformedInput("T1S1");
        transformationRepository.save(transformationEntity);
    }

}
