package com.santandar.trivia;

import com.santandar.trivia.controller.TriviaController;
import com.santandar.trivia.exception.CustomException;
import com.santandar.trivia.exception.GlobalExceptionHandler;
import com.santandar.trivia.model.Trivia;
import com.santandar.trivia.repo.TriviaRepository;
import com.santandar.trivia.service.TriviaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
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
        mockMvc = MockMvcBuilders.standaloneSetup(triviaController).build();
    }

    @Test
    public void testStartTrivia() throws Exception {
        // Mock service behavior here

        mockMvc.perform(post("/trivia/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.question").exists());
    }

    @Test
    public void testReplyTriviaRight() throws Exception {
        Trivia trivia = new Trivia("Question", "Answer");
        trivia.setTriviaId(1L);

        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.of(trivia));
        when(triviaServiceImpl.saveTrivia(trivia)).thenReturn(trivia);

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"Answer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Right!"));

        verify(triviaServiceImpl).deleteTrivia(trivia);
    }

    @Test
    public void testReplyTriviaWrong() throws Exception {
        Trivia trivia = new Trivia("Question", "Answer");
        trivia.setTriviaId(1L);
        trivia.incrementAttempts();

        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.of(trivia));
        when(triviaServiceImpl.saveTrivia(trivia)).thenReturn(trivia);

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"WrongAnswer\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("Wrong!"));

        verify(triviaServiceImpl).saveTrivia(trivia);
    }

    @Test
    public void testReplyTriviaNotFound() throws Exception {
        when(triviaServiceImpl.findTriviaById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"answer\":\"Answer\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.result").value("No such question!"));
    }
}