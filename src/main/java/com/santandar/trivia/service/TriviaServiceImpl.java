package com.santandar.trivia.service;

import com.santandar.trivia.dto.ExternalAPIResponse;
import com.santandar.trivia.dto.TriviaGameReplyRequest;
import com.santandar.trivia.dto.TriviaGameStartResponse;
import com.santandar.trivia.model.Trivia;
import com.santandar.trivia.repo.TriviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TriviaServiceImpl implements TriviaService {
    @Autowired
    private TriviaRepository triviaRepository;

    public Optional<Trivia> findTriviaById(Long id) {
        return triviaRepository.findById(id);
    }

    @Override
    public Trivia saveTrivia(Trivia trivia) {
        return triviaRepository.save(trivia);
    }

    public void deleteTrivia(Trivia trivia) {
        triviaRepository.delete(trivia);
    }

    public List<Trivia> getAllTrivia() {
        return triviaRepository.findAll();
    }

    @Override
    public TriviaGameStartResponse startTrivia() {
            TriviaGameStartResponse gameStartResponse;

            String externalAPIUrl = "https://opentdb.com/api.php?amount=1";
            RestTemplate restTemplate = new RestTemplate();

            ExternalAPIResponse response = restTemplate.getForObject(externalAPIUrl, ExternalAPIResponse.class);

            Optional<ExternalAPIResponse.ResultsDTO> resultsResponse = Optional.ofNullable(response.getResults().get(0));

            String question = String.valueOf(Optional.empty());
            String correctAnswer = String.valueOf(Optional.empty());
            List<String> incorrectAnswers = new ArrayList<>();

            question = resultsResponse.get().getQuestion();
            correctAnswer = resultsResponse.get().getCorrectAnswer();
            incorrectAnswers = resultsResponse.get().getIncorrectAnswers();

            // Insert question into DB
            Trivia trivia = Trivia.builder().question(question).correctAnswer(correctAnswer).build();
            triviaRepository.save(trivia);

            // Shuffle answers
            List<String> possibleAnswers = new ArrayList<>(incorrectAnswers);
            possibleAnswers.add(correctAnswer);
            Collections.shuffle(possibleAnswers);

            gameStartResponse = TriviaGameStartResponse.builder().triviaId(trivia.getTriviaId()).question(question).possibleAnswers(possibleAnswers).build();

        return gameStartResponse;
    }

    @Override
    public String replyTrivia(Long id, @RequestBody TriviaGameReplyRequest reply) {
        Optional<Trivia> triviaOptional = findTriviaById(id);

        if (triviaOptional.isPresent()) {
            Trivia trivia = triviaOptional.get();
            String userAnswer = reply.getAnswer();

            // Check if max attempts reached
            if (trivia.getAnswerAttempts() >= 3) {
                return "Maximum attempt reached";
            }

            // Check answer
            if (trivia.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
                deleteTrivia(trivia);
                return "Right!";
            } else {
                trivia.incrementAttempts();
                //updated Trivia entity
                saveTrivia(trivia);

                if (trivia.getAnswerAttempts() >= 3) {
                    return "Maximum attempt reached";
                }

                return "Wrong!";
            }
        } else {
            return "No such question!";
        }
    }
}
