package com.Inholland.NovaBank.cucumber.steps;

import com.Inholland.NovaBank.model.DTO.TransactionRequestDTO;
import com.Inholland.NovaBank.model.IBANRequestBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransactionStepDefinitions extends BaseStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;


    private final HttpHeaders httpHeaders = new HttpHeaders();
    private String jwtToken;

    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;


    @Given("The user is logged in with username {string} and the password {string}")
public void theUserIsLoggedInWithUsernameAndThePassword(String arg0, String arg1) {
        response = restTemplate
                .exchange(restTemplate.getRootUri() + "/auth/login",
                        HttpMethod.POST,
                        new HttpEntity<>(Map.of("username", arg0, "password", arg1), httpHeaders),
                        String.class);
        jwtToken = JsonPath.read(response.getBody(), "$.token");
    }

    @Given("The endpoint for {string} is available with method {string}")
    public void theEndpointForIsAvailableWithMethod(String arg0, String arg1) {
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        response = restTemplate
                .exchange("/" + arg0,
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(null, httpHeaders), // null because OPTIONS does not have a body
                        String.class);
        List<String> options = Arrays.stream(response.getHeaders()
                        .get("Allow")
                        .get(0)// The first element is all allowed methods separated by comma
                        .split(","))
                .toList();

        Assertions.assertTrue(options.contains(arg1.toUpperCase()));
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
                .bodyValue(new TransactionRequestDTO(arg0, arg1, Float.parseFloat(arg2), arg3))
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .block();

    }

    @When("I retrieve all transactions from user with userId {string}")
    public void iRetrieveAllTransactionsFromUserWithUserId(String arg0) {
        response = restTemplate
                .exchange(restTemplate.getRootUri() + "/transactions/user/" + arg0,
                        HttpMethod.GET,
                        new HttpEntity<>(null, httpHeaders),
                        String.class);
    }

    @When("I withdraw {string} from account with IBAN {string}")
    public void iWithdrawFromAccountWithIBAN(String arg0, String arg1) {
        new TransactionRequestDTO(arg1, arg1, Float.parseFloat(arg0), "withdraw");
        WebClient webClient = WebClient.builder()
                .baseUrl(restTemplate.getRootUri() + "/transactions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

        response = webClient.post()
                .bodyValue(new TransactionRequestDTO(arg1, arg1, Float.parseFloat(arg0), "withdraw"))
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .block();
    }


    @When("I deposit {string} to account with IBAN {string}")
    public void iDepositToAccountWithIBAN(String arg0, String arg1) {
        new TransactionRequestDTO(arg1, arg1, Float.parseFloat(arg0), "deposit");
        WebClient webClient = WebClient.builder()
                .baseUrl(restTemplate.getRootUri() + "/transactions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

        response = webClient.post()
                .bodyValue(new TransactionRequestDTO(arg1, arg1, Float.parseFloat(arg0), "deposit"))
                .exchangeToMono(clientResponse -> clientResponse.toEntity(String.class))
                .block();
    }

    @Then("I should receive all transactions")
    public void iShouldReceiveAllTransactions() {
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        int size = JsonPath.read(response.getBody(), "$.length()");
        Assertions.assertEquals(7, size);
    }

    @Then("I should receive a transaction with amount {string} and description {string} from account with IBAN {string} to account with IBAN {string}")
    public void iShouldReceiveATransactionWithAmountAndDescriptionFromAccountWithIBANToAccountWithIBAN(String arg0, String arg1, String arg2, String arg3) {
    }

    @Then("I should recieve all transactions from user with userId {string}")
    public void iShouldRecieveAllTransactionsFromUserWithUserId(String arg0) {
    }

    @Then("I should recieve a transaction with amount {string} and description {string} from account with IBAN {string} to account with IBAN {string} and direction {string}")
    public void iShouldRecieveATransactionWithAmountAndDescriptionFromAccountWithIBANToAccountWithIBANAndDirection(String arg0, String arg1, String arg2, String arg3, String arg4) {
    }
}
