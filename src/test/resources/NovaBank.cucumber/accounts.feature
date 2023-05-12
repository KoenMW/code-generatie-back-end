Feature: accounts CRUD operations

  Scenario: Getting all cars
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then I should receive all accounts


  Scenario: Create an account
    Given The endpoint for "accounts" is available for method "POST"
    When I create an account with userId 23 and accountType "SAVINGS" and an absoluteLimit of 100
    Then The response status is 201
    And The response body is a JSON object containing a property "iban" and a property "accountType" with value "SAVINGS"