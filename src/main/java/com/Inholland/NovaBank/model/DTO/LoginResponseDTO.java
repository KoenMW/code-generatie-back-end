package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class LoginResponseDTO extends BaseDTO {
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public LoginResponseDTO() {
    }
}
