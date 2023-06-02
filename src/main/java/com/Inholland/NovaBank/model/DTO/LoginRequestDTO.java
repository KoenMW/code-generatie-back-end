package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO(String username, String password){
        this.username = username;
        this.password = password;
    }
    public LoginRequestDTO(){}
}
