package com.shebele.wallet.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class TransactionId {
    private final String value;

    private TransactionId(String value) {
        this.value = value;
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID().toString());
    }

    public static TransactionId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TransactionId cannot be null or blank");
        }
        return new TransactionId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionId that = (TransactionId) o;
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