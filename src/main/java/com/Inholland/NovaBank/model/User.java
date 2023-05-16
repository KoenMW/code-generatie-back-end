package com.Inholland.NovaBank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
@Table(name="\"Users\"")
public class User {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Account> bankAccounts;

    @GeneratedValue
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String role;
    private int dayLimit;
    private int transactionLimit;
    private boolean hasAccount;

    public User(List<Account> bankAccounts, String firstName, String lastName, String userName, String password, String email, String role, int dayLimit, int transactionLimit, boolean hasAccount) {
        this.bankAccounts = bankAccounts;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
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
