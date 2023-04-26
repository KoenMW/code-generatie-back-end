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

    public User(String firstName, String lastName, List<Account> bankAccounts) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccounts = bankAccounts;
    }

    public User() {

    }


}
