package com.Inholland.NovaBank.controller;

import com.Inholland.NovaBank.configuration.ApiTestConfiguration;
import com.Inholland.NovaBank.model.DTO.newUserDTO;
import com.Inholland.NovaBank.model.DTO.patchUserDTO;
import com.Inholland.NovaBank.model.DTO.returnUserDTO;
import com.Inholland.NovaBank.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
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
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(ApiTestConfiguration.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getById() throws Exception {
        when(userService.getById(1L)).thenReturn(new returnUserDTO( 1L, "John", "Doe", "JohnDoe", "John@doe.nl", null, 1000, 1000, true));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("userId", "1");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getByIdInvallid() throws Exception {
        when(userService.getById(89L)).thenThrow(new IllegalArgumentException("User not found"));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users/89")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("userId", "89");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getAll() throws Exception {
        when(userService.getAll(true, 100L, 0L)).thenReturn(List.of(new returnUserDTO(1L, "John", "Doe", "JohnDoe", "john@doe.com", null, 1000, 1000, true)));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("isActive", "true")
                        .param("limit", "100")
                        .param("offset", "0");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getAllInvalid() throws Exception {
        when(userService.getAll(true, 100L, 0L)).thenReturn(null);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("isActive", "true")
                        .param("limit", "100")
                        .param("offset", "0");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void update() throws Exception{
        when(userService.update(any(patchUserDTO.class))).thenReturn(new returnUserDTO(1L, "John", "Doe", "JohnDoe", "john@doe.nl", null, 1000, 1000, true));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "id": "1",
                                    "op": "update",
                                    "key": "firstName",
                                    "value": "Henkie"
                                  }
                                """);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void updateInvalid() throws Exception {
        when(userService.update(any(patchUserDTO.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "id": "1",
                                    "op": "update",
                                    "key": "firstName",
                                    "value": "Henkie"
                                  }
                                """);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void add() throws Exception {
        when(userService.addUser(any(newUserDTO.class))).thenReturn(new returnUserDTO(1L, "John", "Doe", "JohnDoe", "john@doe.nl", null, 1000, 1000, true));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "username": "JohnDoe",
                                    "email": "john@doe.nl",
                                    "password": "1234"
                                 }
                                 """);

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.firstName").value("John"));
    }
    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void addInvalid() throws Exception {
        when(userService.addUser(any(newUserDTO.class))).thenReturn(null);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "username": "JohnDoe",
                                    "email": "john@doe.nl,
                                    "password": "1234"
                                 }""");

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getRemainingDailyLimit() throws Exception{
        when(userService.getRemainingDailyLimit(1L)).thenReturn(1000.00);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users/dailylimit/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("userId", "1");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1000L));
    }
    @Test
    @WithMockUser(username = "JohnDoe", password = "123h4jg893n",roles = "ADMIN")
    void getRemainingDailyLimitInvalid() throws Exception{
        when(userService.getRemainingDailyLimit(1L)).thenThrow(new IllegalArgumentException());

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/users/dailylimit/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param("userId", "1");

        this.mockMvc.perform(builder).andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("$").doesNotExist());
    }


}
