package com.Inholland.NovaBank.cucumber.steps;

import com.Inholland.NovaBank.model.Account;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;

import java.util.Arrays;
import java.util.List;

public class AccountStepDefinitions extends BaseStepDefinitions{
    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();

    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;
    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        response = restTemplate
                .exchange("/" + endpoint,
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(null, httpHeaders), // null because OPTIONS does not have a body
                        String.class);
        List<String> options = Arrays.stream(response.getHeaders()
                        .get("Allow")
                        .get(0)// The first element is all allowed methods separated by comma
                        .split(","))
                .toList();
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("I create an account with userId {int} and accountType {string} and an absoluteLimit of {int}")
    public void iCreateAnAccountWithUserIdAndAccountTypeAndAnAbsoluteLimitOf(int userId, String accountType, int absoluteLimit) throws JsonProcessingException {

        newAccountDTO dto = new newAccountDTO(userId,AccountType.valueOf(accountType),absoluteLimit);
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange("/accounts",
                HttpMethod.POST,
                new HttpEntity<>(
                        mapper.writeValueAsString(dto),
                        httpHeaders
                ), String.class);
    }


    @Then("The response status is {int}")
    public void theResponseStatusIs(int status) {

        Assertions.assertEquals(status, response.getStatusCode().value());
    }

    @And("The response body is a JSON object containing a property {string} and a property {string} with value {string}")
    public void theResponseBodyIsAJSONObjectContainingAPropertyAndAPropertyWithValue(String iban, String accountType, String type) throws JsonProcessingException {

        returnAccountDTO account = mapper.readValue(response.getBody(), returnAccountDTO.class);
        Assertions.assertNotNull(account.getIban());
        Assertions.assertEquals(type, account.getAccountType().toString());


    }


    @When("I retrieve all accounts")
    public void iRetrieveAllAccounts() {
        response = restTemplate.exchange(restTemplate.getRootUri() + "/accounts", HttpMethod.GET, new HttpEntity<>(null, new HttpHeaders()), String.class);
    }

    @Then("I should receive all accounts")
    public void iShouldReceiveAllAccounts() {
        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(3, actual);
        Assertions.assertEquals(200, response.getStatusCode().value());
    }
}

