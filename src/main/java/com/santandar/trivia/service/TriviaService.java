package com.santandar.trivia.service;

import com.santandar.trivia.dto.TriviaGameReplyRequest;
import com.santandar.trivia.dto.TriviaGameStartResponse;
import com.santandar.trivia.model.Trivia;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public interface TriviaService {
    public TriviaGameStartResponse startTrivia();

    public String replyTrivia(Long id, @RequestBody TriviaGameReplyRequest reply);

    // Find Trivia by ID
    public Optional<Trivia> findTriviaById(Long id);

    // Save updated trivia (attempt count)
    public Trivia saveTrivia(Trivia trivia) ;

    // Delete Trivia if the answer is correct
    public void deleteTrivia(Trivia trivia) ;
}
