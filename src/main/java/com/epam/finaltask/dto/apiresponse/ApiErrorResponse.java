package com.epam.finaltask.dto.apiresponse;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ApiErrorResponse {
    private int statusCode;
    private String errorMessage;
    private String failureReason;
    private String path;
    private String method;

    private String errorCode;
    private List<FieldErrorDto> fieldErrors = new ArrayList<>();
}
