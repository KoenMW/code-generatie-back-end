package com.Inholland.NovaBank.model.DTO;

import lombok.Data;

@Data
public class ErrorDTO extends BaseDTO{
    private String message;
    private int code;

    public ErrorDTO(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ErrorDTO() {

    }
}
