package com.santandar.trivia.service;

import com.santandar.trivia.repo.TriviaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

public class TriviaServiceImplTest {
@MockBean
private TriviaRepository repository;

private TestRestTemplate testRestTemplate;

@BeforeEach
void setUp() {
}

}