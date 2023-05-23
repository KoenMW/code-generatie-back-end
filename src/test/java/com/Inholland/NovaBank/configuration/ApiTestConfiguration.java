package com.Inholland.NovaBank.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
@TestConfiguration
public class ApiTestConfiguration {
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

}
