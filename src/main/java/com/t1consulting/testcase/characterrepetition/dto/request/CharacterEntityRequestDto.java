package com.t1consulting.testcase.characterrepetition.dto.request;


import lombok.*;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEntityRequestDto {
    @NonNull
    @Size(max = 5000, min = 1)
    private String text;

    @Override
    public String toString() {
        return "CharacterEntityRequestDto{" +
                "text length ='" + text.length() + '\'' +
                '}';
    }
}
