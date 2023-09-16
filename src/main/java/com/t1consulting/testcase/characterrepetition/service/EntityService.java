package com.t1consulting.testcase.characterrepetition.service;

import com.t1consulting.testcase.characterrepetition.dto.request.CharacterEntityRequestDto;
import com.t1consulting.testcase.characterrepetition.dto.response.RepetitionsEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.model.CharacterEntity;

import javax.validation.Valid;

public interface EntityService {
    CharacterEntity add(@Valid CharacterEntityRequestDto requestDto);

    RepetitionsEntityResponseDto getRepetitionsById(Long id);

    RepetitionsEntityResponseDto getRepetitions(CharacterEntityRequestDto requestDto);

    RepetitionsEntityResponseDto getRepetitions(String text);
}
