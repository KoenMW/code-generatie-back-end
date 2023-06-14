Feature: users crud and login

  Scenario: Get user by id
    Given The user is already logged in with username "JohnDoe" and password "1234567"
    Given The endpoint "users" is available for method "GET"
    When I retrieve the user with id 1
    Then I should receive the user with id 1

  Scenario: Getting all users
    Given The user is already logged in with username "JohnDoe" and password "1234567"
    Given The endpoint "users" is available for method "GET"
    When I retrieve all users
    Then I should receive a list of users

  Scenario: find all users without account
    Given The user is already logged in with username "JohnDoe" and password "1234567"
    Given The endpoint "users" is available for method "GET"
    When I retrieve all users without account with hasAccount is "true"
    Then The response status code is 200
    And I should receive all users without account

  Scenario: Add user
    Given The user is already logged in with username "JohnDoe" and password "1234567"
    Given The endpoint "users" is available for method "POST"
    When I create a user with firstName "Henk", lastName "Doe", username "henkDoe", password "12345678" and email "Henk@doe.nl"
    Then The response status code is 201
    And the response body is a JSON object containing a property "dayLimit" with value 5000

  Scenario: find user dayLimit by id
    Given The user is already logged in with username "JohnDoe" and password "1234567"
    Given The endpoint "users" is available for method "GET"
    When I retrieve the dayLimit from the user with id 1
    Then I should receive the dayLimit 5000 from the user

  Scenario: Update user
    Given The user is already logged in with username "JohnDoe" and password "1234567"
    Given The endpoint "users" is available for method "PATCH"
    When I update the user with id 3 and set the dayLimit to 1234
    Then The response status code is 200