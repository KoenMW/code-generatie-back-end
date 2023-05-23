package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.Role;
import lombok.Data;

@Data
public class patchUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
    private int dayLimit;
    private int transactionLimit;
    private int currentDay;

    public patchUserDTO(String firstName, String lastName, String username, String email, Role role, int dayLimit, int transactionLimit, int currentDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.currentDay = currentDay;
    }
    public patchUserDTO() {

    }
}