package com.santandar.trivia.repo;

import com.santandar.trivia.model.Trivia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriviaRepository extends JpaRepository<Trivia, Long> {
}
