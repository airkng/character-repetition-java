package com.t1consulting.testcase.characterrepetition.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.t1consulting.testcase.characterrepetition.dto.request.CharacterEntityRequestDto;
import com.t1consulting.testcase.characterrepetition.dto.response.RepetitionsEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.exceptions.IllegalFormatException;
import com.t1consulting.testcase.characterrepetition.exceptions.IllegalFormatRequestBodyException;
import com.t1consulting.testcase.characterrepetition.exceptions.ReadRequestBodyException;
import com.t1consulting.testcase.characterrepetition.mapper.CharacterEntityMapper;
import com.t1consulting.testcase.characterrepetition.model.Format;
import com.t1consulting.testcase.characterrepetition.service.EntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RequiredArgsConstructor
@RestController
@RequestMapping("/characters")
@Validated
@Slf4j
public class CharacterEntityController {
    private final EntityService service;
    private final CharacterEntityMapper entityMapper;

    @PostMapping(consumes = {"application/json", "text/plain"}, produces = {"application/json", "text/plain"})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addEntityToDb(final ServletRequest request,
                                           @RequestHeader(name = "answer-format", required = false, defaultValue = "TEXT") final Format format) {
        log.info("Запрос на создание сущности текста");
        CharacterEntityRequestDto requestDto = readBody(request);
        var entity = service.add(requestDto);
        var response = entityMapper.toEntityResponseDto(entity);
        if (format.name().equalsIgnoreCase("text")) {
            return ResponseEntity.created(URI.create("")).header("id", String.valueOf(entity.getId())).contentType(TEXT_PLAIN).body(response.getText());
        } else if (format.name().equalsIgnoreCase("json")) {
            return ResponseEntity.created(URI.create("")).header("id", String.valueOf(entity.getId())).contentType(APPLICATION_JSON).body(response);
        } else {
            log.info("Введен несуществующий формат получения данных в header:answer-format");
            throw new IllegalFormatException("Несуществующий формат получения данных");
        }

    }

    @GetMapping("/repetitions/{id}")
    public ResponseEntity<?> getRepetitionsById(@PathVariable(name = "id", required = true) @Positive final Long id,
                                                @RequestHeader(name = "answer-format", required = false, defaultValue = "TEXT") final Format format) {
        log.info("Запрос на получение результата сущности с id = {}", id);
        var result = service.getRepetitionsById(id);

        if (format.name().equalsIgnoreCase("text")) {
            return ResponseEntity.created(URI.create("")).contentType(TEXT_PLAIN).body(result.getResult());
        } else if (format.name().equalsIgnoreCase("json")) {
            return ResponseEntity.created(URI.create("")).contentType(APPLICATION_JSON).body(result);
        } else {
            log.info("Введен несуществующий формат получения данных в header:answer-format");
            throw new IllegalFormatException("Несуществующий формат получения данных");
        }
    }

    @GetMapping(value = "/repetitions", consumes = {"application/json", "text/plain"}, produces = {"application/json", "text/plain"})
    public ResponseEntity<?> getRepetitionNow(final ServletRequest request,
                                              @RequestHeader(name = "answer-format", required = false, defaultValue = "TEXT") final Format format,
                                              @RequestParam(name = "text", required = false) @Valid @Size(max = 50, min = 1) final String text) {
        log.info("Запрос на выполнение расчета повтора с текстом длины)");

        CharacterEntityRequestDto requestDto = readBody(request);
        RepetitionsEntityResponseDto response = null;
        if (requestDto == null && text == null) {
            throw new IllegalArgumentException("Неверный формат данных");
        }
        if (text != null) {
            if (requestDto != null) {
                if (text.equals(requestDto.getText())) {
                    response = service.getRepetitions(requestDto);
                } else {
                    throw new IllegalArgumentException("Неверный");
                }
            } else {
                response = service.getRepetitions(text);
            }
        }
        if (requestDto != null) {
            response = service.getRepetitions(requestDto);
        }

        if (format.name().equalsIgnoreCase("text")) {
            return ResponseEntity.created(URI.create("")).contentType(TEXT_PLAIN).body(response.getResult());
        } else if (format.name().equalsIgnoreCase("json")) {
            return ResponseEntity.created(URI.create("")).contentType(APPLICATION_JSON).body(response);
        } else {
            log.info("Введен несуществующий формат получения данных в header:answer-format");
            throw new IllegalFormatException("Несуществующий формат получения данных");
        }
    }

    private CharacterEntityRequestDto readBody(final ServletRequest request) {
        CharacterEntityRequestDto requestDto;
        String type = request.getContentType();
        try (BufferedReader reader = request.getReader()) {
            switch (type) {
                case "application/json" -> {
                    ObjectMapper mapper = new ObjectMapper();
                    requestDto = mapper.readValue(reader, CharacterEntityRequestDto.class);
                }
                case "text/plain" -> {
                    String info = reader.readLine();
                    requestDto = entityMapper.toEntityRequestDto(info);
                }
                case "none" -> requestDto = null;
                default -> throw new IllegalFormatRequestBodyException(String.format("Неверный формат данных клиента %s", type));
            }
        } catch (IOException ex) {
            log.warn("ПРОБЛЕМА СЧИТЫВАНИЯ http request body ServletRequest(класс)");
            log.warn(ex.getMessage());
            throw new ReadRequestBodyException("Ошибка на стороне серввера");
        }
        return requestDto;
    }

}
