package com.Inholland.NovaBank.controllers;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getAll() throws Exception {

        // Arrange
        when(accountService.getAll(false,1000L,0L))
                .thenReturn(List.of(new Account("NL18INHO0363662776",200,2,AccountType.SAVINGS,true,200)));


        this.mockMvc.perform(get("/accounts?offset=1000&limit=0&isActive=false")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].iban").value("NL18INHO0363662776"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void add() throws Exception {

        // Arrange
        when(accountService.add(any(newAccountDTO.class))).thenReturn(new returnAccountDTO("NL18INHO0363662776",AccountType.SAVINGS));

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
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }


}
