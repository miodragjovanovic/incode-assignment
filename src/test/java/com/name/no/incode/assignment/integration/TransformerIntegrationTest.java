package com.name.no.incode.assignment.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.name.no.incode.assignment.dto.request.TransformRequest;
import com.name.no.incode.assignment.dto.request.Transformer;
import com.name.no.incode.assignment.dto.response.Transformation;
import com.name.no.incode.assignment.model.TransformationEntity;
import com.name.no.incode.assignment.repository.TransformationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static com.name.no.incode.assignment.enums.TransformerType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class TransformerIntegrationTest extends BaseIntegrationTest {

    public static final String INPUT = "TeSt";
    public static final String UPPERCASE_OUTPUT = "TEST";
    public static final String TRANSFORMATIONS_PATH = "/api/transformations";
    public static final String TRANSFORM_PATH = "/api/transform";
    public static final String VALID_REGEX = "[a-z]";
    public static final String INVALID_REGEX = "[a-z";
    public static final String REPLACEMENT = "1";
    public static final String FROM = "from";
    public static final String TO = "to";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransformationRepository transformationRepository;

    private static final LocalDateTime DATE_TIME = LocalDateTime.now();

    @Test
    void testTransformSuccessUppercase() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(UPPERCASE, null, null))
        );

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertEquals(UPPERCASE_OUTPUT, response);
    }

    @Test
    void testTransformSuccessLowercase() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(LOWERCASE, null, null))
        );

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertEquals("test", response);
    }

    @Test
    void testTransformSuccessRegexRemove() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(REGEX_REMOVE, VALID_REGEX, null))
        );

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertEquals("TS", response);
    }

    @Test
    void testTransformSuccessRegexReplace() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(REGEX_REPLACE, VALID_REGEX, REPLACEMENT))
        );

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertEquals("T1S1", response);
    }

    @Test
    void testTransformSuccess() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(LOWERCASE, null, null),
                        new Transformer(UPPERCASE, null, null),
                        new Transformer(REGEX_REPLACE, VALID_REGEX, REPLACEMENT)
                )
        );

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
        String response = mvcResult.getResponse().getContentAsString();
        assertNotNull(response);
        assertEquals(UPPERCASE_OUTPUT, response);
    }

    @Test
    void testTransformInvalidInput() throws Exception {
        TransformRequest request = new TransformRequest(
                null,
                List.of(new Transformer(REGEX_REPLACE, VALID_REGEX, REPLACEMENT))
        );

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testTransformInvalidTransformations() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                null
        );

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testTransformInvalidRegex() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(REGEX_REPLACE, INVALID_REGEX, REPLACEMENT))
        );

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testTransformInvalidRegexReplace() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(REGEX_REPLACE, VALID_REGEX, null))
        );

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testTransformInvalidRegexRemove() throws Exception {
        TransformRequest request = new TransformRequest(
                INPUT,
                List.of(new Transformer(REGEX_REPLACE, null, null))
        );

        mockMvc.perform(MockMvcRequestBuilders.post(TRANSFORM_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testFetchTransformationsSuccess() throws Exception {
        TransformationEntity transformationEntity = new TransformationEntity();
        transformationEntity.setInput(INPUT);
        transformationEntity.setTransformerType(UPPERCASE);
        transformationEntity.setTransformedInput(UPPERCASE_OUTPUT);
        transformationEntity.setRegex(null);
        transformationEntity.setReplacement(null);
        transformationRepository.save(transformationEntity);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TRANSFORMATIONS_PATH)
                        .param(FROM, DATE_TIME.toString())
                        .param(TO, DATE_TIME.plusDays(1).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertNotNull(mvcResult.getResponse());
        String responseJson = mvcResult.getResponse().getContentAsString();
        assertNotNull(responseJson);
        List<Transformation> transformations = objectMapper.readValue(responseJson, new TypeReference<>() {});

        assertEquals(1, transformations.size());
        Transformation result = transformations.getFirst();
        assertEquals(UPPERCASE, result.getTransformerType());
        assertEquals(INPUT, result.getInput());
        assertEquals(UPPERCASE_OUTPUT, result.getTransformedInput());
    }

    @Test
    void testFetchTransformationsInvalidDateRange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(TRANSFORMATIONS_PATH)
                        .param(FROM, DATE_TIME.toString())
                        .param(TO, DATE_TIME.minusDays(1).toString()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @AfterEach
    void cleanUp() {
        transformationRepository.deleteAll();
    }

}
