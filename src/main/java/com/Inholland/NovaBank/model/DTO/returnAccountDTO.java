package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.AccountType;
import lombok.Data;

@Data
public class returnAccountDTO extends BaseDTO {
    private String iban;
    private AccountType accountType;

    public returnAccountDTO(String iban, AccountType accountType) {
        this.iban = iban;
        this.accountType = accountType;
    }

    public returnAccountDTO() {

    }
}
