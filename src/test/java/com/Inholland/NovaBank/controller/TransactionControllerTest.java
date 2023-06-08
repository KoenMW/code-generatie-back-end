package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.service.AccountService;
import com.Inholland.NovaBank.service.BaseService;
import com.Inholland.NovaBank.service.OffsetBasedPageRequest;
import com.Inholland.NovaBank.service.TransactionService;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
@Import(ApiTestConfiguration.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getAllWithCorrect() throws Exception {
        when(transactionService.GetAll()).thenReturn(List.of(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "-")));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        this.mockMvc.perform(builder).andDo(print())

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getAllWithNoRole() throws Exception {
        when(transactionService.GetAll()).thenReturn(List.of(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "-")));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



    @Test
    @WithMockUser(username = "JohnDoe", roles = "ADMIN")
    void getAllFromIban() {
        when(transactionService.GetAllFromIban(any(String.class))).thenReturn(List.of(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "-")));

        String Iban = BaseService.generateIban();
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/transactions/byIban")
                        .content("""
{
                                    "iban": "%s"
                                }
                                """.formatted(Iban)
                        )
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        try {
            this.mockMvc.perform(builder)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Test
    @WithMockUser(username = "JohnDoe", roles = "ADMIN")
    void getAllFromInvalidIban() {
        when(transactionService.GetAllFromIban(any(String.class))).thenReturn(List.of(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "-")));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/transactions/byIban")
                        .content("""
{
                                    "iban": "NL01INHO0000000001"
                                }
                                """
                        )
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        try {
            this.mockMvc.perform(builder)
                    .andDo(print())
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void add() throws Exception {
        when(transactionService.ValidateTransaction(any(TransactionRequestDTO.class))).thenReturn(true);
        when(transactionService.Add(any(TransactionRequestDTO.class))).thenReturn(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"));

        this.mockMvc.perform(post("/transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                                {
                                	"fromAccount": "NL33INHO0317308340",
                                	"toAccount": "NL55INHO0324345488",
                                	"amount": 50,
                                	"description": "test"
                                }
                                """
                        )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void addWithInvalidTransaction() throws Exception {
        when(transactionService.ValidateTransaction(any(TransactionRequestDTO.class))).thenReturn(false);
        when(transactionService.Add(any(TransactionRequestDTO.class))).thenReturn(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"));

        this.mockMvc.perform(post("/transactions").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                	"fromAccount": "NL33INHO0317308340",
                                	"toAccount": "NL55INHO0324345488",
                                	"amount": 50,
                                	"description": "test"
                                }
                                """
                        )
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getAllFromUser() {
        when(transactionService.GetAllFromUser(any(Long.class))).thenReturn(List.of(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "+"), new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",100, "test", LocalDateTime.now(), "-")));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/transactions/byUser/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE);

        try {
            this.mockMvc.perform(builder)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void withdraw() throws Exception {
        when(transactionService.ValidateWithdraw(any(DepositWithdrawDTO.class))).thenReturn(true);
        when(transactionService.Withdraw(any(DepositWithdrawDTO.class))).thenReturn(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "-"));

        this.mockMvc.perform(post("/transactions/withdraw").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                	"iban": "NL33INHO0317308340",
                                	"amount": 50
                                }
                                """
                        )
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void deposit() throws Exception {
        when(transactionService.ValidateDeposit(any(DepositWithdrawDTO.class))).thenReturn(true);
        when(transactionService.Deposit(any(DepositWithdrawDTO.class))).thenReturn(new TransactionResponceDTO("NL01INHO0000000001","NL01INHO0000000002",10, "test", LocalDateTime.now(), "+"));

        this.mockMvc.perform(post("/transactions/deposit").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                	"iban": "NL33INHO0317308340",
                                	"amount": 50
                                }
                                """
                        )
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}