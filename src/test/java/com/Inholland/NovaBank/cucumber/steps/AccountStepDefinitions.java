package com.Inholland.NovaBank.cucumber.steps;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.configuration.JwtCucumberConf;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.Role;
import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;



public class AccountStepDefinitions extends BaseStepDefinitions{


    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();
    @Autowired
    private JwtCucumberConf jwtCucumberConf;





    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;


    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        //httpHeaders.add("Authorization", "Bearer " + jwtCucumberConf.jwtToken);
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
    public void iCreateAnAccountWithUserIdAndAccountTypeAndAnAbsoluteLimitOf(int userId, String accountType, int absoluteLimit, int dailyLimit, int transactionLimit) throws JsonProcessingException {

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


    @When("I retrieve the account with userReferenceId {int}")
    public void iRetrieveTheAccountWithUserReferenceId(int userId) {
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


