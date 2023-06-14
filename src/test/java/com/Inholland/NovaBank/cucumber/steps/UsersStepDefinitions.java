package com.Inholland.NovaBank.cucumber.steps;

import com.Inholland.NovaBank.model.DTO.*;
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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UsersStepDefinitions extends BaseStepDefinitions{

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String jwtToken;
    private ResponseEntity<String> response;

    //Login
    @Given("The user is already logged in with username {string} and password {string}")
    public void theUserIsAlreadyLoggedInWithUsernameAndPassword(String username, String password) throws JsonProcessingException {
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange("/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(
                        mapper.writeValueAsString(Map.of("username", username, "password", password)),
                        httpHeaders
                ), String.class);
        jwtToken = JsonPath.read(response.getBody(), "$.token");
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
    }

    @Given("The endpoint {string} is available for method {string}")
    public void theEndpointIsAvailableForMethod(String endpoint, String method) {
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        response = restTemplate
                .exchange("/" + endpoint,
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(httpHeaders),
                        String.class);
    }
    //Find user by id
    @When("I retrieve the user with id {int}")
    public void iRetrieveTheUserWithId(int id) {
        response = restTemplate
                .exchange("/users/" + id,
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        String.class);
    }

    @Then("I should receive the user with id {int}")
    public void iShouldReceiveTheUserWithId(int id) {
        Map<String, Object> user = JsonPath.read(response.getBody(), "$");
        assert user.get("id").equals(id);
    }

    //Find all users
    @When("I retrieve all users")
    public void iRetrieveAllUsers() {
        response = restTemplate
                .exchange("/users",
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        String.class);
    }
    @Then("I should receive a list of users")
    public void iShouldReceiveAListOfUsers() throws JsonProcessingException {
        Assertions.assertEquals(200, response.getStatusCode().value());
        int actual = JsonPath.read(response.getBody(), "$.size()");

        Assertions.assertEquals(3, actual);
    }

    //Find dayLimit by id
    @When("I retrieve the dayLimit from the user with id {int}")
    public void iRetrieveTheDayLimitFromTheUserWithId(long id) {
        response = restTemplate
                .exchange("/users/dailylimit/" + id,
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        String.class);
    }

    @Then("I should receive the dayLimit {int} from the user")
    public void iShouldReceiveTheDayLimitFromTheUserWithId(int dayLimit) {
        Assertions.assertEquals(200, response.getStatusCode().value());
        // get json body from response
        Double getDayLimit = JsonPath.read(response.getBody(), "$");
        // assert dayLimit
        Assertions.assertEquals(dayLimit, getDayLimit);
    }

    //Find all users without account
    @When("I retrieve all users without account with hasAccount is {string}")
    public void iRetrieveAllUsersWithoutAccountWithHasAccountIs(String bool) {
        response = restTemplate
                .exchange("/users?isActive=" + bool,
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        String.class);
    }
    @And("I should receive all users without account")
    public void iShouldReceiveAllUsersWithoutAccount() {
        Assertions.assertEquals(200, response.getStatusCode().value());
        int actual = JsonPath.read(response.getBody(), "$.size()");

        Assertions.assertEquals(1, actual);
    }

    //Update user
    @When("I update the user with id {int} and set the dayLimit to {int}")
    public void iUpdateTheUserWithIdAndSetTheDayLimitTo(long id, int dayLimit) throws JsonProcessingException {
        patchUserDTO dto = new patchUserDTO();
        dto.setKey("dayLimit");
        dto.setValue(String.valueOf(dayLimit));
        dto.setOp("update");
        dto.setId(id);

        WebClient client = WebClient.builder()
                .baseUrl(restTemplate.getRootUri() + "/users")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

        response = (ResponseEntity<String>) client.patch()
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .block()
                .toEntity(String.class)
                .block();
        System.out.println(response.getBody());
    }

    //add user
    @When("I create a user with firstName {string}, lastName {string}, username {string}, password {string} and email {string}")
    public void iCreateAUserWithFirstNameLastNameUsernamePasswordAndEmail(String firstName, String lastName, String username, String password, String email) throws JsonProcessingException {
        newUserDTO userDTO = new newUserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setEmail(email);

        response = restTemplate
                .exchange("/users",
                        HttpMethod.POST,
                        new HttpEntity<>(
                                mapper.writeValueAsString(userDTO),
                                httpHeaders
                        ), String.class);
    }
    @Then("The response status code is {int}")
    public void theResponseStatusCodeIs(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCode().value());
    }
    @And("the response body is a JSON object containing a property {string} with value {int}")
    public void theResponseBodyIsAJSONObjectContainingAPropertyWithValue(String dayLimit, int value) {
        Map<String, Object> user = JsonPath.read(response.getBody(), "$");
        assert user.get(dayLimit).equals(value);
    }

}

