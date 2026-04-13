package com.shebele.wallet.domain.repository;

import com.shebele.wallet.domain.model.Account;
import com.shebele.wallet.domain.valueobject.AccountId;
import com.shebele.wallet.domain.valueobject.PhoneNumber;

import java.util.Optional;

public interface AccountAggregateRepository {
    Optional<Account> findById(AccountId id);
    Optional<Account> findByMsisdn(PhoneNumber msisdn);
    void save(Account account);
    boolean existsByMsisdn(PhoneNumber msisdn);
}