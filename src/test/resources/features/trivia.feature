Feature: Trivia Game

  Scenario: Get a new trivia question
    Given the trivia service is available
    When I request a new trivia question
    Then I should receive a trivia question and possible answers