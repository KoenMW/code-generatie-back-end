package com.Inholland.NovaBank.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Data
@Table(name="\"Users\"")
public class User {

    @GeneratedValue
    @Id
    private Long id;
    private String firstName;
    private String lastName;

    private String username;
    private String password;
    private String email;
    private Role role;
    private int dayLimit;
    private int transactionLimit;
    private boolean hasAccount;

    public User(String firstName, String lastName, String username, String password, String email, Role role, int dayLimit, int transactionLimit, boolean hasAccount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.hasAccount = hasAccount;
    }

    public User() {

    }
}
