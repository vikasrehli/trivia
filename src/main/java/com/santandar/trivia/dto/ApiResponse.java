package com.santandar.trivia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T>  {
    private String message;
    private String status;
    private Integer code;
    private T data;
}
