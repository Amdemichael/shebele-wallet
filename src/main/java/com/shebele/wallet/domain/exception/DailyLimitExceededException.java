package com.shebele.wallet.domain.exception;

import com.shebele.wallet.domain.valueobject.Money;

public class DailyLimitExceededException extends DomainException {
    private final Money limit;
    private final Money todayTotal;
    private final Money requested;

    public DailyLimitExceededException(Money limit, Money todayTotal, Money requested) {
        super(String.format("Daily limit exceeded. Limit: %s, Today: %s, Requested: %s", limit, todayTotal, requested));
        this.limit = limit;
        this.todayTotal = todayTotal;
        this.requested = requested;
    }
}