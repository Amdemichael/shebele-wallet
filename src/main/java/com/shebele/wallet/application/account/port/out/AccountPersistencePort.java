package com.shebele.wallet.application.port.out;

import com.shebele.wallet.domain.model.Account;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import java.util.Optional;

public interface AccountPersistencePort {
    Optional<Account> findByMsisdn(PhoneNumber msisdn);
    void save(Account account);
    boolean existsByMsisdn(PhoneNumber msisdn);
}