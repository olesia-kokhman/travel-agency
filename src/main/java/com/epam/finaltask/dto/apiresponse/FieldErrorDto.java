package com.epam.finaltask.dto.apiresponse;

import lombok.Data;

@Data
public class FieldErrorDto {

    private String field;
    private String message;
    private String rejected;
}
