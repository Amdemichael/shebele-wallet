package com.shebele.wallet.helper;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Helper class for monetary amount operations.
 */
@Component
public class AmountHelper {

    private static final BigDecimal MINIMUM_TRANSFER = new BigDecimal("0.01");
    private static final BigDecimal MAXIMUM_TRANSFER = new BigDecimal("1000000");
    private static final int DECIMAL_PLACES = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Validates if amount is valid for transfer
     */
    public boolean isValidAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }

        return amount.compareTo(MINIMUM_TRANSFER) >= 0 &&
                amount.compareTo(MAXIMUM_TRANSFER) <= 0;
    }

    /**
     * Validates amount with custom limits
     */
    public boolean isValidAmount(BigDecimal amount, BigDecimal min, BigDecimal max) {
        if (amount == null || min == null || max == null) {
            return false;
        }

        return amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
    }

    /**
     * Formats amount for display (e.g., 1000.50 → "1,000.50 Birr")
     */
    public String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00 Birr";
        }

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(DECIMAL_PLACES);
        formatter.setMaximumFractionDigits(DECIMAL_PLACES);

        return formatter.format(amount) + " Birr";
    }

    /**
     * Formats amount without currency (e.g., 1000.50 → "1,000.50")
     */
    public String formatAmountOnly(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(DECIMAL_PLACES);
        formatter.setMaximumFractionDigits(DECIMAL_PLACES);

        return formatter.format(amount);
    }

    /**
     * Rounds amount to 2 decimal places
     */
    public BigDecimal roundAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }

        return amount.setScale(DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Converts amount to cents (integer representation)
     */
    public long toCents(BigDecimal amount) {
        if (amount == null) {
            return 0;
        }

        return amount.multiply(new BigDecimal("100")).longValue();
    }

    /**
     * Converts from cents to Birr
     */
    public BigDecimal fromCents(long cents) {
        return new BigDecimal(cents).divide(new BigDecimal("100"), DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Calculates percentage of amount
     */
    public BigDecimal calculatePercentage(BigDecimal amount, BigDecimal percentage) {
        if (amount == null || percentage == null) {
            return BigDecimal.ZERO;
        }

        return amount.multiply(percentage).divide(new BigDecimal("100"), DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Gets minimum transfer amount
     */
    public BigDecimal getMinimumTransfer() {
        return MINIMUM_TRANSFER;
    }

    /**
     * Gets maximum transfer amount
     */
    public BigDecimal getMaximumTransfer() {
        return MAXIMUM_TRANSFER;
    }
}