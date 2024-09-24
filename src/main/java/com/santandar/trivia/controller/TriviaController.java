package com.santandar.trivia.controller;

import com.santandar.trivia.dto.ApiResponse;
import com.santandar.trivia.dto.TriviaGameReplyRequest;
import com.santandar.trivia.dto.TriviaGameReplyResponse;
import com.santandar.trivia.dto.TriviaGameStartResponse;
import com.santandar.trivia.exception.CustomException;
import com.santandar.trivia.service.TriviaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/trivia")
@RequiredArgsConstructor
public class TriviaController {
    @Autowired
    public TriviaService triviaService;

    // POST /trivia/start
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<?>> startTrivia() {
        try {
            TriviaGameStartResponse response = triviaService.startTrivia();

            ApiResponse<?> apiResponse = ApiResponse.builder()
                    .code(OK.value())
                    .status("SUCCESS")
                    .message("Trivia game started")
                    .data(response)
                    .build();

            return ResponseEntity.ok(apiResponse);
        } catch (Exception ex){
            throw new CustomException(NOT_FOUND.value(),"No such question!");
        }
    }

    // PUT /trivia/reply/{id}
    @PutMapping("/reply/{id}")
    public ResponseEntity<?> replyTrivia(@PathVariable Long id, @RequestBody TriviaGameReplyRequest reply) {
        String response =  triviaService.replyTrivia(id, reply);
        TriviaGameReplyResponse replyResponse = TriviaGameReplyResponse.builder().result(response).build();

        if("Maximum attempt reached".equalsIgnoreCase(response)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(replyResponse);
        } else if ("Right!".equalsIgnoreCase(response)) {
            return ResponseEntity.status(HttpStatus.OK).body(replyResponse);
        } else if ("Wrong!".equalsIgnoreCase(response)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(replyResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(replyResponse);
        }
      }
}