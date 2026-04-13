package com.shebele.wallet.domain.event;

import com.shebele.wallet.domain.valueobject.AccountId;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import java.time.LocalDateTime;

public class AccountRegisteredEvent implements DomainEvent {
    private final AccountId accountId;
    private final PhoneNumber msisdn;
    private final LocalDateTime occurredAt;

    public AccountRegisteredEvent(AccountId accountId, PhoneNumber msisdn, LocalDateTime occurredAt) {
        this.accountId = accountId;
        this.msisdn = msisdn;
        this.occurredAt = occurredAt;
    }

    public AccountId getAccountId() { return accountId; }
    public PhoneNumber getMsisdn() { return msisdn; }
    @Override
    public LocalDateTime getOccurredAt() { return occurredAt; }
}