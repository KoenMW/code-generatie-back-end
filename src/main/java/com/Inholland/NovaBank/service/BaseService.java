package com.Inholland.NovaBank.service;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.math.BigInteger;

public abstract class BaseService {
    public boolean IsValidIban(String iban) {
        if (iban == null || iban.length() < 15 || iban.length() > 34) {
            return false;
        }
        //move the four initial characters to the end of the string:
        iban = iban.substring(4) + iban.substring(0, 4);
        //replace each letter in the string with two digits, thereby expanding the string, where A = 10, B = 11, ..., Z = 35:
        StringBuilder numericString = new StringBuilder();
        for (int i = 0; i < iban.length(); i++) {
            char c = iban.charAt(i);
            if (Character.isLetter(c)) {
                numericString.append((int) c - 55);
            } else {
                numericString.append(c);
            }
        }
        //interpret the string as a decimal integer and compute the remainder of that number on division by 97:
        BigInteger ibanAsNumber = new BigInteger(numericString.toString());
        return ibanAsNumber.remainder(BigInteger.valueOf(97)).intValue() == 1;
    }

    public static String generateIban() {
        int accountNumber = (int) (Math.random() * 1000000000);
        Iban iban = new Iban.Builder()
                .countryCode(CountryCode.NL)
                .bankCode("INHO")
                .accountNumber(String.format("%010d", accountNumber))
                .buildRandom();
        return iban.toString();
    }
}


