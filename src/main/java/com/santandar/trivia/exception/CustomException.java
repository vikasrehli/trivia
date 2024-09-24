package com.santandar.trivia.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomException extends RuntimeException {
    private Integer code;
    private String message;

}
