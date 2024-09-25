package com.santandar.trivia.steps;

import com.santandar.trivia.model.Trivia;
import com.santandar.trivia.repo.TriviaRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class TriviaSteps {

    private static final String TRIVIA_START_ENDPOINT = "/trivia/start";
    private static final String TRIVIA_REPLY_ENDPOINT_TEMPLATE = "/trivia/reply/%d";
    private static final String APPLICATION_JSON = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TriviaRepository repository;

    private MvcResult result;

    @Given("the trivia game service is available")
    public void the_trivia_game_service_is_available() {
        assertThat(mockMvc).isNotNull();
    }

    @When("I start a new trivia game")
    public void i_start_a_new_trivia_game() throws Exception {
        result = mockMvc.perform(post(TRIVIA_START_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("I should receive a random trivia question with possible answers")
    public void i_should_receive_a_random_trivia_question_with_possible_answers() throws Exception {
        String response = result.getResponse().getContentAsString();
        assertThat(response).contains("question").contains("possibleAnswers");
    }

    @Given("there is an active trivia question")
    public void there_is_an_active_trivia_question() {
        Trivia trivia = Trivia.builder().triviaId(1L).correctAnswer("Chile").answerAttempts(1).build();
        repository.save(trivia);
    }

    @When("I reply with the correct answer")
    public void i_reply_with_the_correct_answer() throws Exception {
        result = mockMvc.perform(put(getReplyEndpoint(1L))
                        .contentType(APPLICATION_JSON)
                        .content("{\"answer\": \"Chile\"}"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Then("I should receive a success message with {string}")
    public void i_should_receive_a_success_message_with(String message) throws Exception {
        assertResponseContainsMessage(message);
    }

    @Given("there is a trivia question to validate wrong answer")
    public void there_is_a_trivia_question() {
        Trivia trivia = Trivia.builder().triviaId(2L).correctAnswer("Chile").answerAttempts(1).build();
        repository.save(trivia);
    }

    @When("I reply with the wrong answer")
    public void i_reply_with_the_wrong_answer() throws Exception {
        result = mockMvc.perform(put(getReplyEndpoint(2L))
                        .contentType(APPLICATION_JSON)
                        .content("{\"answer\": \"Argentina\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Then("I should receive a failure message with {string}")
    public void i_should_receive_a_failure_message_with(String message) throws Exception {
        assertResponseContainsMessage(message);
    }

    @Given("there is an active trivia question with 3 failed attempts")
    public void there_is_an_active_trivia_question_with_3_failed_attempts() {
        Trivia trivia = Trivia.builder().triviaId(3L).correctAnswer("Chile").answerAttempts(3).build();
        repository.save(trivia);
    }

    @When("I reply to the trivia question")
    public void iReplyToTheTriviaQuestion() throws Exception {
        result = mockMvc.perform(put(getReplyEndpoint(3L))
                        .contentType(APPLICATION_JSON)
                        .content("{\"answer\": \"Argentina\"}"))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Then("I should receive a message saying {string}")
    public void i_should_receive_a_message_saying(String message) throws Exception {
        assertResponseContainsMessage(message);
    }

    private String getReplyEndpoint(Long triviaId) {
        return String.format(TRIVIA_REPLY_ENDPOINT_TEMPLATE, triviaId);
    }

    private void assertResponseContainsMessage(String message) throws Exception {
        String response = result.getResponse().getContentAsString();
        assertThat(response).contains(message);
    }
}