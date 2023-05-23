package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class newUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;

    public newUserDTO(String firstName, String lastName, String username, String password, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email=email;
    }
    public newUserDTO() {

    }
}
