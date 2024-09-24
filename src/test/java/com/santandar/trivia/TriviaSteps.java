//package com.santandar.trivia;
//
//import com.santandar.trivia.controller.TriviaController;
//import com.santandar.trivia.service.TriviaService;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(TriviaController.class)
//public class TriviaSteps {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TriviaService triviaService;
//
//    private String response;
//
//    @Given("the trivia service is available")
//    public void the_trivia_service_is_available() {
//        // Ensure the service is running
//    }
//
//    @When("I request a new trivia question")
//    public void i_request_a_new_trivia_question() throws Exception {
//        response = mockMvc.perform(post("/trivia/start"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//    }
//
//    @Then("I should receive a trivia question and possible answers")
//    public void i_should_receive_a_trivia_question_and_possible_answers() throws Exception {
//        mockMvc.perform(get("/trivia/start"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.question").exists())
//                .andExpect(jsonPath("$.possibleAnswers").isArray());
//    }
//}
//
