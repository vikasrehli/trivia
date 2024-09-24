package com.santandar.trivia.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private Integer code;
    private String message;
    private String status;
}
