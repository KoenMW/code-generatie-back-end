package com.Inholland.NovaBank.controllers;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.controller.AuthController;
import com.Inholland.NovaBank.model.DTO.LoginResponseDTO;
import com.Inholland.NovaBank.service.UserService;
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
import com.Inholland.NovaBank.model.DTO.LoginRequestDTO;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(ApiTestConfiguration.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // We mock our service, because we don't want to test it here
    // Note that we have to Mock all dependencies our controller code uses if we use @WebMvcTest
    @MockBean
    private UserService userService;

    @WithMockUser(username = "test", password = "test")
    @Test
    void login() throws Exception {
        when(userService.login(any(LoginRequestDTO.class))).thenReturn(new LoginResponseDTO("iqwugeuqigweuq"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "username": "test",
                                    "password": "test"
                                  }
                                """))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    void loginWithInvalidCredentials() throws Exception {
        // We mock the login method to return null, because we want to test the controller, not the service
        when(userService.login(any(LoginRequestDTO.class))).thenReturn(null);

        // We perform a POST request to /auth/login with a JSON body
        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "username": "test",
                                    "password": "test"
                                  }
                                """))

                .andDo(print())
                .andExpect(jsonPath("$.token").doesNotExist());




    }
}
