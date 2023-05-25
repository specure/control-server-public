package com.specure.response.sah;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorContainerResponse {
    @JsonProperty(value = "error")
    private final List<String> error = new ArrayList<>();

    public void addErrorString(String errorMessage) {
        this.error.add(errorMessage);
    }

    public ErrorContainerResponse(String errorMessage) {
        addErrorString(errorMessage);
    }

    public static ErrorContainerResponse empty() {
        return new ErrorContainerResponse();
    }
}
