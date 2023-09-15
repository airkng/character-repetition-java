package com.t1consulting.testcase.characterrepetition.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionEntity {
    private String message;
    private String cause;
}
