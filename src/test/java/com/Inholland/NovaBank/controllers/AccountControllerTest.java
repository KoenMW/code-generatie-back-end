package com.Inholland.NovaBank.controllers;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.service.AccountService;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.file.Path;
import java.util.List;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@Import(ApiTestConfiguration.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // We mock our service, because we don't want to test it here
    // Note that we have to Mock all dependencies our controller code uses if we use @WebMvcTest
    @MockBean
    private AccountService accountService;
    @BeforeEach
    void setUp() {


    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROLE_ADMIN"})
    void getAll() throws Exception {

        when(accountService
                .getAll(1000L, 0L))
                .thenReturn(List.of(
                        new Account("NL18INHO0363662776",1000,1, AccountType.CHECKING,true,100)));



        this.mockMvc.perform(get("/accounts")).andDo(print())
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", hasSize(1)))
                .andExpect((ResultMatcher) jsonPath("$[0].accountType").value("CHECKING"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void add() throws Exception {

        // Arrange
        when(accountService.add(any(newAccountDTO.class))).thenReturn(new returnAccountDTO());

        // Act & Assert
        this.mockMvc.perform(post("/accounts").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        /// String literals in Java 17: enclose in """
                        .content("""
                                 {
                                    "userReferenceId": "1",
                                    "accountType": "SAVINGS",
                                    "absoluteLimit": "100"
                                  }
                                """))
                // But since we used any(Car.class) a simple {} should be enough
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.accountType").value("SAVINGS"));
    }
}
