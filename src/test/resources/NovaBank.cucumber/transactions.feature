Feature: transactions CRUD operations

  Scenario: Getting all transactions
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "transactions" is available for method "GET"
    When I retrieve all transactions
    Then I should recieve all transactions

  Scenario: Getting all transactions from a single account
    Given The user is logged in with username "JohnDoe" and password "1234"
    Given The endpoint for "transactions/byIban" is available for method "GET"
    When I retrieve all transactions from account "123456789"
    Then I should recieve all transactions from account "123456789"