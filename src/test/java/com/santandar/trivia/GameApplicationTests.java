package com.santandar.trivia;

import com.santandar.trivia.controller.TriviaController;
import com.santandar.trivia.exception.GlobalExceptionHandler;
import com.santandar.trivia.model.Trivia;
import com.santandar.trivia.repo.TriviaRepository;
import com.santandar.trivia.service.TriviaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TriviaController.class)
@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class GameApplicationTests {

    @InjectMocks
    private TriviaController triviaController;

    @MockBean
    private TriviaRepository repository;

    @SpyBean
    private TriviaServiceImpl triviaServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(triviaController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Set up global exception handler
                .build();
    }

    @Test
    public void shouldStartNewTriviaGame() throws Exception {
        // Mocking service behavior
        mockMvc.perform(post("/trivia/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.question").exists());
    }

    @Test
    public void shouldReturnSuccessMessageForCorrectAnswer() throws Exception {
        // Given an active trivia question in the repository
        Trivia trivia = new Trivia("What is the capital of Chile?", "Santiago");
        trivia.setTriviaId(1L);

        // Mocking service behavior for correct answer
        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.of(trivia));
        when(triviaServiceImpl.saveTrivia(trivia)).thenReturn(trivia);

        // When replying with the correct answer
        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"Santiago\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Right!"));

        // Then verify that the trivia is deleted after a correct answer
        verify(triviaServiceImpl).deleteTrivia(trivia);
    }

    @Test
    public void shouldReturnFailureMessageForIncorrectAnswer() throws Exception {
        // Given an active trivia question in the repository
        Trivia trivia = new Trivia("What is the capital of Chile?", "Santiago");
        trivia.setTriviaId(1L);
        trivia.incrementAttempts(); // Incrementing attempt for wrong answers

        // Mocking service behavior for wrong answer
        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.of(trivia));
        when(triviaServiceImpl.saveTrivia(trivia)).thenReturn(trivia);

        // When replying with the wrong answer
        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"Buenos Aires\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("Wrong!"));

        // Then verify that trivia is still saved (and not deleted)
        verify(triviaServiceImpl).saveTrivia(trivia);
    }

    @Test
    public void shouldReturnNotFoundForNonExistentTriviaQuestion() throws Exception {
        // Given that the trivia question does not exist
        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.empty());

        // When trying to reply to a non-existent question
        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"Santiago\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value("No such question!"));
    }

    @Test
    public void shouldHandleMaxAttemptsExceeded() throws Exception {
        // Given a trivia question with max attempts exceeded
        Trivia trivia = new Trivia("What is the capital of Chile?", "Santiago");
        trivia.setTriviaId(1L);
        trivia.setAnswerAttempts(3); // Simulate 3 failed attempts

        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.of(trivia));

        // When replying after max attempts
        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"Buenos Aires\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.result").value("Maximum attempt reached"));
    }
}