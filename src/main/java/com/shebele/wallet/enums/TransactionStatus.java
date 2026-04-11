package com.shebele.wallet.enums;

public enum TransactionStatus {
    PENDING("Pending", "Transaction initiated but not completed"),
    COMPLETED("Completed", "Transaction successfully completed"),
    FAILED("Failed", "Transaction failed"),
    REVERSED("Reversed", "Transaction was reversed");

    private final String displayName;
    private final String description;

    TransactionStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static TransactionStatus fromDisplayName(String displayName) {
        for (TransactionStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        return PENDING;
    }
}