package com.shebele.wallet.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class PhoneNumber {

    private static final Pattern ETHIOPIAN_PHONE_PATTERN = Pattern.compile("251[0-9]{9}");
    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }
        String normalized = normalize(value);
        if (!ETHIOPIAN_PHONE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid Ethiopian phone number: " + value);
        }
        return new PhoneNumber(normalized);
    }

    private static String normalize(String phoneNumber) {
        String cleaned = phoneNumber.replaceAll("[^0-9]", "");
        if (cleaned.startsWith("0") && cleaned.length() == 10) {
            return "251" + cleaned.substring(1);
        }
        if (cleaned.startsWith("251") && cleaned.length() == 12) {
            return cleaned;
        }
        if (cleaned.length() == 9) {
            return "251" + cleaned;
        }
        return cleaned;
    }

    public String getValue() {
        return value;
    }

    public String getMasked() {
        return value.substring(0, 3) + "****" + value.substring(7);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}