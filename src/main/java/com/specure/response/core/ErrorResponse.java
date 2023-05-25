package com.specure.response.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {

    private final String message;

    @JsonProperty(value = "error")
    private final List<String> error;

    public ErrorResponse(String errorMessage) {
        this.message = errorMessage;
        this.error = List.of(errorMessage);
    }

}
