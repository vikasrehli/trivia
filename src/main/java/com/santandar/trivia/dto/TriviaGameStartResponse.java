package com.santandar.trivia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TriviaGameStartResponse {
    private Long triviaId;

    private String question;

    private List<String> possibleAnswers;
}
