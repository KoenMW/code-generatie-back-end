Feature: transactions CRUD operations

  Scenario: Getting all transactions
    Given The user is logged in with username "JohnDoe" and the password "1234567"
    Given The endpoint for "transactions" is available with method "GET"
    When I retrieve all transactions
    Then I should receive all transactions

  Scenario: Adding a transaction
    Given The user is logged in with username "JohnDoe" and the password "1234567"
    Given The endpoint for "transactions" is available with method "POST"
    When I add a transaction from account with IBAN "NL12INHO0154160635" to account with IBAN "NL18INHO0363662776" with amount "100" and description "test"
    Then I should receive a transaction with amount "100" and description "test" from account with IBAN "NL12INHO0154160635" to account with IBAN "NL18INHO0363662776"

  Scenario: getting all transactions from a single user
    Given The user is logged in with username "JohnDoe" and the password "1234567"
    Given The endpoint for "transactions/byUser/1" is available with method "GET"
    When I retrieve all transactions from user with userId "1"
    Then I should receive all transactions from user with userId "1"

  Scenario: Withdrawing money from an account
    Given The user is logged in with username "JohnDoe" and the password "1234567"
    Given The endpoint for "transactions/withdraw" is available with method "POST"
    When I withdraw "100" from account with IBAN "NL12INHO0154160635"
    Then I should receive a withdraw with the IBAN "NL12INHO0154160635" and amount "100"

    Scenario: Depositing money to an account
    Given The user is logged in with username "JohnDoe" and the password "1234567"
    Given The endpoint for "transactions/deposit" is available with method "POST"
    When I deposit "100" to account with IBAN "NL12INHO0154160635"
    Then I should receive a deposit with the IBAN "NL12INHO0154160635" and amount "100"