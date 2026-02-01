package com.epam.finaltask.dto;

import com.epam.finaltask.exception.FieldErrorDto;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ApiErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String method;

    private String errorCode;
    private List<FieldErrorDto> fieldErrors = new ArrayList<>();
}
