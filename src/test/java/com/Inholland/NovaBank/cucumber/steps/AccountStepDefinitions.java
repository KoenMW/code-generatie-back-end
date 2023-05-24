package com.Inholland.NovaBank.cucumber.steps;

import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
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
import org.springframework.http.*;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class AccountStepDefinitions extends BaseStepDefinitions{


    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String jwtToken;





    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;


    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
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

    @Given("The user is logged in with username {string} and password {string}")
    public void theUserIsLoggedInWithUsernameAndPassword(String username, String password) throws JsonProcessingException {
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange("/login",
                HttpMethod.POST,
                new HttpEntity<>(
                        mapper.writeValueAsString(Map.of("username", username, "password", password)),
                        httpHeaders
                ), String.class);
        jwtToken = JsonPath.read(response.getBody(), "$.token");
    }




    @When("I create an account with userId {int} and accountType {string} and an absoluteLimit of {int}")
    public void iCreateAnAccountWithUserIdAndAccountTypeAndAnAbsoluteLimitOf(int userId, String accountType, int absoluteLimit) throws JsonProcessingException {
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        newAccountDTO dto = new newAccountDTO(userId, AccountType.valueOf(accountType),absoluteLimit);
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
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        response = restTemplate.exchange(restTemplate.getRootUri() + "/accounts", HttpMethod.GET, new HttpEntity<>(null, new HttpHeaders()), String.class);
    }

    @Then("I should receive all accounts")
    public void iShouldReceiveAllAccounts() {
        Assertions.assertEquals(200, response.getStatusCode().value());
        int actual = JsonPath.read(response.getBody(), "$.size()");

        Assertions.assertEquals(3, actual);

    }


    @When("I retrieve the account with userReferenceId {int}")
    public void iRetrieveTheAccountWithUserReferenceId(int userId) {
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        response = restTemplate.exchange(restTemplate.getRootUri() + "/accounts/" + userId, HttpMethod.GET, new HttpEntity<>(null, new HttpHeaders()), String.class);
    }

    @And("There is an account with property {string}")
    public void thereIsAnAccountWithProperty(String iban) {
        //Loop trough response assert true if iban is found
        List<returnAccountDTO> accounts = Arrays.asList(mapper.convertValue(JsonPath.read(response.getBody(), "$"), returnAccountDTO[].class));
        Assertions.assertNotNull(accounts.stream().anyMatch(account -> Boolean.parseBoolean(account.getIban())));
    }

    @When("I patch the account with iban {string} and an absoluteLimit of {int}")
    public void iPatchTheAccountWithIbanAndAnAbsoluteLimitOf(String iban, int absoluteLimit) throws IOException {
        patchAccountDTO dto = new patchAccountDTO();
        dto.setKey("absoluteLimit");
        dto.setValue(String.valueOf(absoluteLimit));
        dto.setOp("update");
        dto.setIban(iban);




    }

    @And("The response body is a JSON object containing a property {string}")
    public void theResponseBodyIsAJSONObjectContainingAProperty(String iban) {
        returnAccountDTO account = null;
        try {
            account = mapper.readValue(response.getBody(), returnAccountDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert account != null;
        Assertions.assertNotNull(account.getIban());
    }
}


