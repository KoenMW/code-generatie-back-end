package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class DepositWithdrawDTO extends BaseDTO {
    private String iban;
    private float amount;
    private long userId;

    public DepositWithdrawDTO(String iban, float amount, long userId) {
        this.iban = iban;
        this.amount = amount;
        this.userId = userId;
    }
    public DepositWithdrawDTO() {

    }
}
