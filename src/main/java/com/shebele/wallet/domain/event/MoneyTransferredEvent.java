package com.shebele.wallet.domain.event;

import com.shebele.wallet.domain.valueobject.AccountId;
import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import java.time.LocalDateTime;

public class MoneyTransferredEvent implements DomainEvent {
    private final AccountId fromAccountId;
    private final AccountId toAccountId;
    private final PhoneNumber fromMsisdn;
    private final PhoneNumber toMsisdn;
    private final Money amount;
    private final Money fee;
    private final Money newFromBalance;
    private final Money newToBalance;
    private final LocalDateTime occurredAt;

    public MoneyTransferredEvent(AccountId fromAccountId, AccountId toAccountId,
                                 PhoneNumber fromMsisdn, PhoneNumber toMsisdn,
                                 Money amount, Money fee, Money newFromBalance, Money newToBalance) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.fromMsisdn = fromMsisdn;
        this.toMsisdn = toMsisdn;
        this.amount = amount;
        this.fee = fee;
        this.newFromBalance = newFromBalance;
        this.newToBalance = newToBalance;
        this.occurredAt = LocalDateTime.now();
    }

    // Getters
    public AccountId getFromAccountId() { return fromAccountId; }
    public AccountId getToAccountId() { return toAccountId; }
    public PhoneNumber getFromMsisdn() { return fromMsisdn; }
    public PhoneNumber getToMsisdn() { return toMsisdn; }
    public Money getAmount() { return amount; }
    public Money getFee() { return fee; }
    public Money getNewFromBalance() { return newFromBalance; }
    public Money getNewToBalance() { return newToBalance; }
    @Override
    public LocalDateTime getOccurredAt() { return occurredAt; }
}