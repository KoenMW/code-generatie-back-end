package com.Inholland.NovaBank.controllers;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AccountController;
import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.AccountType;
import com.Inholland.NovaBank.model.DTO.newAccountDTO;
import com.Inholland.NovaBank.model.DTO.patchAccountDTO;
import com.Inholland.NovaBank.model.DTO.returnAccountDTO;
import com.Inholland.NovaBank.model.DTO.searchAccountDTO;
import com.Inholland.NovaBank.service.AccountService;
import com.Inholland.NovaBank.service.OffsetBasedPageRequest;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
        when(accountService.getAll(true,100L,0L)).thenReturn(List.of(new Account("NL18INHO0363662776",200,2,AccountType.SAVINGS,true,200)));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("limit", "100")
                        .param("offset", "0")
                        .param("isActive", "true");


        this.mockMvc.perform(builder).andDo(print())

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].iban").value("NL18INHO0363662776"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllWithIncorrect() throws Exception {
        // Arrange
        when(accountService.getAll(true,100L,0L)).thenReturn(null);

        // Act & Assert
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/accounts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("limit", "100")
                        .param("offset", "0")
                        .param("isActive", "true");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addWithIncorrect() throws Exception {
        // Arrange
        when(accountService.add(any(newAccountDTO.class))).thenReturn(null);

        // Act & Assert
        this.mockMvc.perform(post("/accounts").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        /// String literals in Java 17: enclose in """
                        .content("""
                                 {
                                    "userReferenceId": "1",
                                    "accountType": "",
                                    "absoluteLimit": "100"
                                  }
                                """))
                // But since we used any(Car.class) a simple {} should be enough
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update() throws Exception {

        // Arrange
        when(accountService.update(any(patchAccountDTO.class))).thenReturn(new returnAccountDTO("NL18INHO0363662776",AccountType.SAVINGS));

        // Act & Assert
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/accounts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "iban": "NL18INHO0363662776",
                                    "op": "update",
                                    "key": "accountType",
                                    "value": "SAVINGS"
                                  }
                                """);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));


    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateWithIncorrect() throws Exception {
        // Arrange
        when(accountService.update(any(patchAccountDTO.class))).thenReturn(null);

        // Act & Assert
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/accounts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "iban": "NL18INHO0363662776",
                                    "op": "update",
                                    "key": "accountType",
                                    "value": "
                                  }
                                """);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByuserId() throws Exception {
        when(accountService.getByUserId(1L)).thenReturn(List.of(new Account("NL18INHO0363662776",200,2,AccountType.SAVINGS,true,200)));
        this.mockMvc.perform(get("/accounts/1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].iban").value("NL18INHO0363662776"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByInvalidUserId() throws Exception {
        when(accountService.getByUserId(1L)).thenReturn(List.of());
        this.mockMvc.perform(get("/accounts/1")).andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllSearch() throws Exception {
        given(accountService.getAllSearch(100L, 0L)).willReturn(List.of(new searchAccountDTO("NL18INHO0363662776",2,AccountType.SAVINGS)));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/accounts/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("limit", "100")
                        .param("offset", "0");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].iban").value("NL18INHO0363662776"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllSearchInvalide() throws Exception {
        given(accountService.getAllSearch(1000L, 0L)).willReturn(List.of());
        this.mockMvc.perform(get("/accounts/search")).andDo(print())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
