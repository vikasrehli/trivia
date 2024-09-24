package com.santandar.trivia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trivia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long triviaId;
    public String question;
    public String correctAnswer;
    public int answerAttempts;

//    public Trivia() {
//    }

    public Trivia(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.answerAttempts = 0;
    }

//    public Long getTriviaId() {
//        return triviaId;
//    }
//
//    public String getQuestion() {
//        return question;
//    }
//
//    public String getCorrectAnswer() {
//        return correctAnswer;
//    }
//
//    public int getAnswerAttempts() {
//        return answerAttempts;
//    }

    public void incrementAttempts() {
        this.answerAttempts++;
    }

//    public void setTriviaId(Long triviaId) {
//        this.triviaId = triviaId;
//    }
//
//    public void setQuestion(String question) {
//        this.question = question;
//    }
//
//    public void setCorrectAnswer(String correctAnswer) {
//        this.correctAnswer = correctAnswer;
//    }
}
