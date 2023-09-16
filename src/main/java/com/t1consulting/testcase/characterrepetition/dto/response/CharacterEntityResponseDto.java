package com.t1consulting.testcase.characterrepetition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CharacterEntityResponseDto {
    private String text;
}
