package com.epam.finaltask.exception;

import lombok.Data;

@Data
public class FieldErrorDto {

    private String field;
    private String message;
    private String rejected;
}
