package com.santandar.trivia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ExternalAPIResponse {
    @JsonProperty("response_code")
    private Integer responseCode;

    @JsonProperty("results")
    private List<ResultsDTO> results;

    @NoArgsConstructor
    @Data
    public static class ResultsDTO {
        @JsonProperty("category")
        private String category;

        @JsonProperty("type")
        private String type;

        @JsonProperty("difficulty")
        private String difficulty;

        @JsonProperty("question")
        private String question;

        @JsonProperty("correct_answer")
        private String correctAnswer;

        @JsonProperty("incorrect_answers")
        private List<String> incorrectAnswers;
    }
}
