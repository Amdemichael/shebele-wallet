package com.shebele.wallet.enums;

public enum AccountStatus {
    ACTIVE("Active", "Account is fully operational"),
    SUSPENDED("Suspended", "Account temporarily disabled"),
    CLOSED("Closed", "Account permanently closed"),
    FROZEN("Frozen", "Account frozen due to security concerns");

    private final String displayName;
    private final String description;

    AccountStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static AccountStatus fromDisplayName(String displayName) {
        for (AccountStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        return ACTIVE; // Default
    }
}