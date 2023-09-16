package com.t1consulting.testcase.characterrepetition.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t1consulting.testcase.characterrepetition.dto.request.CharacterEntityRequestDto;
import com.t1consulting.testcase.characterrepetition.dto.response.CharacterEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.dto.response.RepetitionsEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.mapper.CharacterEntityMapper;
import com.t1consulting.testcase.characterrepetition.model.CharacterEntity;
import com.t1consulting.testcase.characterrepetition.service.EntityService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CharacterEntityController.class})
@AutoConfigureMockMvc
public class CharacterEntityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntityService service;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CharacterEntityMapper entityMapper;

    @Test
    public void addEntity_jsonFormat_shouldReturnJsonEntity() throws Exception {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("example")
                .build();
        CharacterEntity response = CharacterEntity.builder()
                .id(1L)
                .text("example")
                .build();
        CharacterEntityResponseDto responseDto = CharacterEntityResponseDto.builder()
                .text(response.getText())
                .build();
        when(service.add(request)).thenReturn(response);
        when(entityMapper.toEntityResponseDto(response)).thenReturn(responseDto);
        mockMvc.perform(post("/characters")
                .header("answer-format", "JSON")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("id", "1"))
                .andExpect(jsonPath("$.text", Matchers.is(response.getText())));
    }

    @Test
    public void addEntity_json_shouldReturn_plainText() throws Exception {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("ee")
                .build();
        CharacterEntity response = CharacterEntity.builder()
                .id(2L)
                .text("ee")
                .build();
        CharacterEntityResponseDto responseDto = CharacterEntityResponseDto.builder()
                .text(response.getText())
                .build();
        when(service.add(request)).thenReturn(response);
        when(entityMapper.toEntityResponseDto(response)).thenReturn(responseDto);
        mockMvc.perform(post("/characters")
                        .header("answer-format", "TEXT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("id", "2"))
                .andExpect(content().string(response.getText()));
    }

    @Test
    public void addEntity_incorrectFormat_throwsException() throws Exception {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("ee")
                .build();
        CharacterEntity response = CharacterEntity.builder()
                .id(2L)
                .text("ee")
                .build();
        CharacterEntityResponseDto responseDto = CharacterEntityResponseDto.builder()
                .text(response.getText())
                .build();
        when(service.add(request)).thenReturn(response);
        when(entityMapper.toEntityResponseDto(response)).thenReturn(responseDto);
        mockMvc.perform(post("/characters")
                        .header("answer-format", "LOL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRepetitionsById_shouldReturnJsonText() throws Exception {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("ee")
                .build();

        RepetitionsEntityResponseDto responseDto = RepetitionsEntityResponseDto.builder()
                .repetitions(Map.of('e', 2))
                .build();

        when(service.getRepetitionsById(3L)).thenReturn(responseDto);
        mockMvc.perform(get("/characters/repetitions/3")
                        .header("answer-format", "JSON"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repetitions.e", Matchers.is(2)));
    }

    @Test
    public void getRepetitions_correctValues_shouldReturnJsonText() throws Exception {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("ee")
                .build();

        RepetitionsEntityResponseDto responseDto = RepetitionsEntityResponseDto.builder()
                .repetitions(Map.of('e', 2))
                .build();

        when(service.getRepetitions(request)).thenReturn(responseDto);
        mockMvc.perform(get("/characters/repetitions")
                        .header("answer-format", "JSON")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repetitions.e", Matchers.is(2)));
    }

    @Test
    public void getRepetitions_requestFromQueryParam_shouldReturnJsonText() throws Exception {
        CharacterEntityRequestDto request = CharacterEntityRequestDto.builder()
                .text("ee")
                .build();

        RepetitionsEntityResponseDto responseDto = RepetitionsEntityResponseDto.builder()
                .repetitions(Map.of('e', 2))
                .build();

        when(service.getRepetitions(request)).thenReturn(responseDto);
        when(service.getRepetitions("ee")).thenReturn(responseDto);
        mockMvc.perform(get("/characters/repetitions")
                        .header("answer-format", "JSON")
                        .queryParam("text", "ee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repetitions.e", Matchers.is(2)));
    }


}
