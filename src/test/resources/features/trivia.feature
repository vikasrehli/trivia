Feature: Trivia Game
  As a trivia player,
  I want to answer trivia questions,
  So that I can test my knowledge and track my performance.

  Background:
    Given the trivia game service is available

  Scenario: Start a new trivia game
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