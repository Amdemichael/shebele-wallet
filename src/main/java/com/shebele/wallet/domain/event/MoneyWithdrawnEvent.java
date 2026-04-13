package com.shebele.wallet.domain.event;

import com.shebele.wallet.domain.valueobject.AccountId;
import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import java.time.LocalDateTime;

public class MoneyWithdrawnEvent implements DomainEvent {
    private final AccountId accountId;
    private final PhoneNumber msisdn;
    private final Money amount;
    private final Money oldBalance;
    private final Money newBalance;
    private final String reference;
    private final LocalDateTime occurredAt;

    public MoneyWithdrawnEvent(AccountId accountId, PhoneNumber msisdn, Money amount,
                               Money oldBalance, Money newBalance, String reference) {
        this.accountId = accountId;
        this.msisdn = msisdn;
        this.amount = amount;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.reference = reference;
        this.occurredAt = LocalDateTime.now();
    }

    // Getters
    public AccountId getAccountId() { return accountId; }
    public PhoneNumber getMsisdn() { return msisdn; }
    public Money getAmount() { return amount; }
    public Money getOldBalance() { return oldBalance; }
    public Money getNewBalance() { return newBalance; }
    public String getReference() { return reference; }
    @Override
    public LocalDateTime getOccurredAt() { return occurredAt; }
}