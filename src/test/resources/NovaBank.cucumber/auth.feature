Feature: Auth controller

  Scenario: Login with valid credentials
    When The user logs in with username "JohnDoe" and password "1234"
    Then The response status code should be 200
    And The response should contain a valid JWT token



  Scenario: Login with invalid credentials
    When The user logs in with username "JohnDoe" and password "12345"
    Then The response status code should be 403
    And The response should contain an error message "Password is incorrect"