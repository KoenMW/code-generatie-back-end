package com.Inholland.NovaBank.model.DTO;

import com.Inholland.NovaBank.model.AccountType;
import lombok.Data;

@Data
public class searchAccountDTO extends BaseDTO {
    private String iban;
    private long userReferenceId;
    private AccountType accountType;

    public searchAccountDTO(String iban, long userReferenceId, AccountType accountType) {
        this.iban = iban;
        this.userReferenceId = userReferenceId;
        this.accountType = accountType;
    }

    public searchAccountDTO() {
    }
}
