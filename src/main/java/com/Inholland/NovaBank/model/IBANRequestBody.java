package com.Inholland.NovaBank.model;

import lombok.Data;

@Data
public class IBANRequestBody {

    private String iban;

    public IBANRequestBody(String iban) {
        this.iban = iban;
    }

    public IBANRequestBody() {

    }



    public String getIban() {
        return iban;
    }
}
