package com.Inholland.NovaBank.cucumber.steps;

import com.Inholland.NovaBank.model.DTO.DepositWithdrawDTO;
import com.Inholland.NovaBank.model.DTO.TransactionRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TransactionStepDefinitions extends BaseStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;


    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String jwtToken;

    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;

    @Given("The user is logged in with username {string} and the password {string}")
public void theUserIsLoggedInWithUsernameAndThePassword(String username, String password) throws JsonProcessingException {
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

    @Given("The endpoint for {string} is available with method {string}")
    public void theEndpointForIsAvailableWithMethod(String endpoint, String method) {
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        response = restTemplate
                .exchange("/" + endpoint,
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(null, httpHeaders),
                        String.class);
        List<String> options = Arrays.stream(Objects.requireNonNull(response.getHeaders()
                                .get("Allow"))
                        .get(0)
                        .split(","))
                .toList();

        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() {
        response = restTemplate
                .exchange(restTemplate.getRootUri() + "/transactions",
                        HttpMethod.GET,
                        new HttpEntity<>(null, httpHeaders),
                        String.class);

    }

    @When("I add a transaction from account with IBAN {string} to account with IBAN {string} with amount {string} and description {string}")
    public void iAddATransactionFromAccountWithIBANToAccountWithIBANWithAmountAndDescription(String arg0, String arg1, String arg2, String arg3) {
        WebClient webClient = WebClient.builder()
                .baseUrl(restTemplate.getRootUri() + "/transactions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

        response = webClient.post()
                .bodyValue(new TransactionRequestDTO(arg0, arg1, Float.parseFloat(arg2), arg3, 1L))
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .block();

    }

    @When("I retrieve all transactions from user with userId {string}")
    public void iRetrieveAllTransactionsFromUserWithUserId(String arg0) {
        response = restTemplate
                .exchange(restTemplate.getRootUri() + "/transactions/byUser/" + arg0,
                        HttpMethod.GET,
                        new HttpEntity<>(null, httpHeaders),
                        String.class);
    }

    @When("I withdraw {string} from account with IBAN {string}")
    public void iWithdrawFromAccountWithIBAN(String arg0, String arg1) {
        WebClient webClient = WebClient.builder()
                .baseUrl(restTemplate.getRootUri() + "/transactions/withdraw")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

        response = webClient.post()
                .bodyValue(new DepositWithdrawDTO(arg1, Float.parseFloat(arg0), 1L))
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .block();
    }


    @When("I deposit {string} to account with IBAN {string}")
    public void iDepositToAccountWithIBAN(String arg0, String arg1) {
        DepositWithdrawDTO deposit = new DepositWithdrawDTO(arg1, Float.parseFloat(arg0), 1L);
        System.out.println("deposit:" + arg0 + " to account: " + arg1);
        System.out.println(deposit);
        WebClient webClient = WebClient.builder()
                .baseUrl(restTemplate.getRootUri() + "/transactions/deposit")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

        response = webClient.post()
                .bodyValue(deposit)
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .block();
        System.out.println("response: ");
        System.out.println(response.getBody());
        System.out.println("response code: " + response.getStatusCode());
    }

    @Then("I should receive all transactions")
    public void iShouldReceiveAllTransactions() {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }


    @Then("I should receive all transactions from user with userId {string}")
    public void iShouldReceiveAllTransactionsFromUserWithUserId(String arg0) {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains(arg0));
    }

    @Then("I should receive a transaction with amount {string} and description {string} from account with IBAN {string} to account with IBAN {string}")
    public void iShouldReceiveATransactionWithAmountAndDescriptionFromAccountWithIBANToAccountWithIBAN(String arg0, String arg1, String arg2, String arg3) {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains(arg0));
        Assertions.assertTrue(response.getBody().contains(arg1));
        Assertions.assertTrue(response.getBody().contains(arg2));
        Assertions.assertTrue(response.getBody().contains(arg3));

    }

    @Then("I should receive a withdraw with the IBAN {string} and amount {string}")
    public void iShouldReceiveAWithdrawWithTheIBANAndAmount(String arg0, String arg1) {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains(arg0));
        Assertions.assertTrue(response.getBody().contains(arg1));
    }

    @Then("I should receive a deposit with the IBAN {string} and amount {string}")
    public void iShouldReceiveADepositWithTheIBANAndAmount(String arg0, String arg1) {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().contains(arg0));
        Assertions.assertTrue(response.getBody().contains(arg1));
    }
}
