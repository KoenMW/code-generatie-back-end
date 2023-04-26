package com.Inholland.NovaBank.model;

import jakarta.persistence.Entity;
import lombok.Data;



public enum AccountType {
    SAVINGS,
    CHECKING,
    CREDIT
}
