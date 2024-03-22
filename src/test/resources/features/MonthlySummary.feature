Feature: Trainer Monthly Summary

  Scenario: Process training change for authorized request
    Given a valid trainer workload request
    When the authorized client sends the request to process the training change
    Then the training change should be processed successfully

  Scenario: Process training change for unauthorized request
    Given a trainer workload request
    When the unauthorized client sends the request to process the training change
    Then the return should be 403

  Scenario: Calculate monthly report for authorized request
    Given a valid trainer username
    When the authorized client requests the monthly report for the trainer
    Then the response should contain the trainer's training summary
