Feature: Trivia Game

  Scenario: Start a new trivia game
    Given the trivia game service is available
    When I start a new trivia game
    Then I should receive a random trivia question with possible answers

  Scenario: Correctly answering a trivia question
    Given there is an active trivia question
    When I reply with the correct answer
    Then I should receive a success message with "Right!"

  Scenario: Incorrectly answering a trivia question
    Given there is a trivia question to validate wrong answer
    When I reply with the wrong answer
    Then I should receive a failure message with "Wrong!"

  Scenario: Max attempts reached
    Given there is an active trivia question with 3 failed attempts
    When I reply to the trivia question
    Then I should receive a message saying "Maximum attempt reached"