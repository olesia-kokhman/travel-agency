package com.epam.finaltask.dto;

import lombok.Data;

@Data
public class ApiSuccessResponse<T> {

    private String statusCode;
    private String statusMessage;
    private T results;

    public ApiSuccessResponse(String statusCode, String statusMessage, T results) {
        this.statusCode= statusCode;
        this.statusMessage = statusMessage;
        this.results = results;
    }
}
