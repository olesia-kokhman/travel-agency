package com.epam.finaltask.dto.apiresponse;

import lombok.Data;

@Data
public class ApiSuccessResponse<T> {

    private int statusCode;
    private String statusMessage;
    private T results;

    public ApiSuccessResponse(int statusCode, String statusMessage, T results) {
        this.statusCode= statusCode;
        this.statusMessage = statusMessage;
        this.results = results;
    }
}
