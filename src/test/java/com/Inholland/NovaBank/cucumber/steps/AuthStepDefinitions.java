package com.Inholland.NovaBank.cucumber.steps;

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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AuthStepDefinitions extends BaseStepDefinitions{
    @Autowired
    private TestRestTemplate restTemplate;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String jwtToken;

    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;

    @When("The user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) throws JsonProcessingException {

        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange("/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(
                        mapper.writeValueAsString(Map.of("username", username, "password", password)),
                        httpHeaders
                ), String.class);
        if(response.getStatusCodeValue() == 200)
        {
            jwtToken = JsonPath.read(response.getBody(), "$.token");
            httpHeaders.add("Authorization", "Bearer " + jwtToken);
        }

    }


    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCodeValue());
    }


    @And("The response should contain a valid JWT token")
    public void theResponseShouldContainAValidJWTToken() {
        Assertions.assertTrue(jwtToken.length() > 0);
    }

    @And("The response should contain an error message {string}")
    public void theResponseShouldContainAnErrorMessage(String error) {
        Assertions.assertEquals(error, JsonPath.read(response.getBody(), "$.message"));
    }
}
