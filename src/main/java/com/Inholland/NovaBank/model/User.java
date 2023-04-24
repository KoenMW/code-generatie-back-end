package com.Inholland.NovaBank.model;

import jakarta.persistence.OneToMany;

import java.util.List;

public class User {
    @OneToMany(mappedBy = "user")
    private List<Account> bankAccounts;
}
