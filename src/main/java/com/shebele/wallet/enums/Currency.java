package com.shebele.wallet.enums;

import java.math.BigDecimal;

public enum Currency {
    ETB("ETB", "Ethiopian Birr", 2),
    USD("USD", "US Dollar", 2),
    EUR("EUR", "Euro", 2);

    private final String code;
    private final String name;
    private final int decimalPlaces;

    Currency(String code, String name, int decimalPlaces) {
        this.code = code;
        this.name = name;
        this.decimalPlaces = decimalPlaces;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public BigDecimal getMinimumAmount() {
        return new BigDecimal("0.01");
    }

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.code.equalsIgnoreCase(code)) {
                return currency;
            }
        }
        return ETB;
    }
}