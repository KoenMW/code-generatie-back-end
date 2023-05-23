package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.Role;
import lombok.Data;

@Data
public class returnUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
    private int dayLimit;
    private int transactionLimit;
    private boolean hasAccount;

    public returnUserDTO(String firstName, String lastName, String username, String email, Role role, int dayLimit, int transactionLimit, boolean hasAccount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.hasAccount = hasAccount;
    }
    public returnUserDTO() {

    }
}
