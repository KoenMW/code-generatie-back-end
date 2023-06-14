Feature: accounts CRUD operations

  Scenario: Getting all accounts
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then I should receive all accounts

  Scenario: getting all accounts invalid
    Given The user is logged in with username "henk" and password "1234"
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then Then the response status is 403


  Scenario: Create an account
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "POST"
    When I create an account with userId 1 and accountType "SAVINGS" and an absoluteLimit of 100
    Then The response status is 201
    And The response body is a JSON object containing a property "iban" and a property "accountType" with value "SAVINGS"

  Scenario: Create an account invalid
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "POST"
    When I create an account with userId 1 and accountType "SAVINGS" and an absoluteLimit of -100
    Then The response status is 400

  Scenario: Get an account
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve the account with userReferenceId 1
    Then The response status is 200
    And There is an account with property "iban"

  Scenario: Get an account invalid
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "GET"
    When I retrieve the account with userReferenceId 100
    Then The response status is 404

  Scenario: Patch an account
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "PATCH"
    When I patch the account with iban "NL18INHO0363662776" and an absoluteLimit of 200
    Then The response status is 200
    And The response body is a JSON object containing a property "iban"

  Scenario: Patch an account invalid
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts" is available for method "PATCH"
    When I patch the account with iban "NL18INHO0363662776" and an absoluteLimit of -200
    Then The response status is 400

  Scenario: Get all search accounts
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts/search" is available for method "GET"
    When I retrieve all accounts with searchPath
    Then I should receive all searchAccounts

  Scenario: Get all search accounts invalid
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "accounts/search" is available for method "GET"
    When I retrieve all accounts throws an error
    Then The response status is 403

