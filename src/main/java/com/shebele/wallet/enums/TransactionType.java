package com.shebele.wallet.enums;

public enum TransactionType {
    P2P_TRANSFER("P2P Transfer", "Person to person money transfer"),
    DEPOSIT("Deposit", "Cash deposit via agent"),
    WITHDRAWAL("Withdrawal", "Cash withdrawal via agent"),
    BILL_PAYMENT("Bill Payment", "Payment for utilities or services"),
    AIRTIME_PURCHASE("Airtime Purchase", "Buy mobile airtime"),
    VAS_SUBSCRIPTION("VAS Subscription", "Value Added Service subscription");

    private final String displayName;
    private final String description;

    TransactionType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}