package com.t1consulting.testcase.characterrepetition.service;

import com.t1consulting.testcase.characterrepetition.dto.request.CharacterEntityRequestDto;
import com.t1consulting.testcase.characterrepetition.dto.response.CharacterEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.dto.response.RepetitionsEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.exceptions.NotFoundException;
import com.t1consulting.testcase.characterrepetition.mapper.CharacterEntityMapper;
import com.t1consulting.testcase.characterrepetition.model.CharacterEntity;
import com.t1consulting.testcase.characterrepetition.repository.EntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class EntityServiceImpl implements EntityService {
    private final EntityRepository repository;
    private final CharacterEntityMapper mapper;

    @Override
    public CharacterEntity add(CharacterEntityRequestDto requestDto) {
        var entityOpt = repository.findByText(requestDto.getText());
        if (entityOpt.isEmpty()) {
            log.info("Сохранение в бд сущности {}", requestDto);
            return repository.save(mapper.toEntity(requestDto));
        }
        return entityOpt.get();
    }

    @Override
    public RepetitionsEntityResponseDto getRepetitionsById(final Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("CharacterEntity with id = %d not found", id)));
        return findRepetitions(entity.getText());
    }

    private RepetitionsEntityResponseDto findRepetitions(String text) {
        char[] characters = text.toCharArray();
        Map<Character, Integer> map = new HashMap<>();

        for (Character buf : characters) {
            if (map.containsKey(buf)) {
                int count = map.get(buf);
                count++;
                map.put(buf, count);
                continue;
            }
            map.put(buf, 1);
        }
        Map<Character, Integer> sorted = map.entrySet().stream()
                .sorted(Map.Entry.<Character, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));;
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for(var entry : sorted.entrySet()) {
            count++;
            if (count == sorted.size()) {
                builder.append("\"" + entry.getKey() + "\":" + entry.getValue());
                continue;
            }
            builder.append("\"" + entry.getKey() + "\":" + entry.getValue() + ",");
        }
        return RepetitionsEntityResponseDto.builder()
                .result(builder.toString())
                .repetitions(sorted)
                .build();

    }

    @Override
    public RepetitionsEntityResponseDto getRepetitions(CharacterEntityRequestDto requestDto) {
        return findRepetitions(requestDto.getText());
    }

    @Override
    public RepetitionsEntityResponseDto getRepetitions(String text) {
        return findRepetitions(text);
    }
}
