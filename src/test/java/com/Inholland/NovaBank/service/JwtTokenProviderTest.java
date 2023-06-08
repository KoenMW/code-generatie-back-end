package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.Role;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {


    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Test
    public void createToken(){
        given(jwtTokenProvider.createToken("test", Role.ROLE_ADMIN,1)).willReturn("test");
        String token = jwtTokenProvider.createToken("test", Role.ROLE_ADMIN,1);

        assertNotNull(token);

    }

    @Test
    public void createTokenWithNull(){
        given(jwtTokenProvider.createToken(null, null,1)).willReturn(null);
        String token = jwtTokenProvider.createToken(null, null,1);

        assertNull(token);

    }

    @Test
    public void authentication(){
        given(jwtTokenProvider.getAuthentication("test")).willReturn(mock(Authentication.class));
        var auth = jwtTokenProvider.getAuthentication("test");

        assertNotNull(auth);

    }

    @Test
    public void authenticationWithNull(){
        assertThrows(JwtException.class, () -> {
            jwtTokenProvider.getAuthentication(null);
            throw new JwtException("Invalid JWT token");
        });
    }
}
