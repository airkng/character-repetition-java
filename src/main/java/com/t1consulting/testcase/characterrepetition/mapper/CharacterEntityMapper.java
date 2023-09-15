package com.t1consulting.testcase.characterrepetition.mapper;

import com.t1consulting.testcase.characterrepetition.dto.request.CharacterEntityRequestDto;
import com.t1consulting.testcase.characterrepetition.dto.response.CharacterEntityResponseDto;
import com.t1consulting.testcase.characterrepetition.model.CharacterEntity;
import org.springframework.stereotype.Component;

@Component
public class CharacterEntityMapper {
    public CharacterEntity toEntity(final CharacterEntityRequestDto dto) {
        return CharacterEntity.builder()
                .text(dto.getText())
                .build();
    }

    public CharacterEntityRequestDto toEntityRequestDto(final String text) {
        return CharacterEntityRequestDto.builder()
                .text(text)
                .build();
    }

    public CharacterEntityResponseDto toEntityResponseDto(CharacterEntity entity) {
        return CharacterEntityResponseDto.builder()
                .text(entity.getText())
                .build();
    }
}
