package com.shebele.wallet.domain.exception;

import com.shebele.wallet.domain.valueobject.Money;

public class InsufficientBalanceException extends DomainException {
    private final Money available;
    private final Money requested;

    public InsufficientBalanceException(Money available, Money requested) {
        super(String.format("Insufficient balance. Available: %s, Requested: %s", available, requested));
        this.available = available;
        this.requested = requested;
    }

    public Money getAvailable() { return available; }
    public Money getRequested() { return requested; }
}