package com.Inholland.NovaBank.model;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;


public enum AccountType implements GrantedAuthority {
    SAVINGS,
    CHECKING,
    CREDIT;

    @Override
    public String getAuthority() {
        return name();
    }
}
