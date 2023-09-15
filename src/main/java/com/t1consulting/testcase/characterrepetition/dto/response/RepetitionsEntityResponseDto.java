package com.t1consulting.testcase.characterrepetition.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepetitionsEntityResponseDto {
    @JsonIgnore
    private String result;
    private Map<Character, Integer> repetitions;
}
