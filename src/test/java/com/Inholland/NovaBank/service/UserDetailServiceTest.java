package com.Inholland.NovaBank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.Inholland.NovaBank.repositorie.UserRepository;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {
    @MockBean
    private UserDetailService userDetailServiceMock;


    @Test
    void LoadUserByUsername() {
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("henkie")
                        .password("1234")
                        .authorities("USER")
                        .build();
        given(userDetailServiceMock.loadUserByUsername("test")).willReturn(userDetails);
        UserDetails user = userDetailServiceMock.loadUserByUsername("test");
        assert(user.getUsername().equals("henkie"));

    }

    @Test
    void LoaduserByUsernameIncorrect(){
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername("henkie")
                        .password("1234")
                        .authorities("USER")
                        .build();
        given(userDetailServiceMock.loadUserByUsername("test")).willReturn(userDetails);
        UserDetails user = userDetailServiceMock.loadUserByUsername("henkie");
        assert(user == null);
    }




}
