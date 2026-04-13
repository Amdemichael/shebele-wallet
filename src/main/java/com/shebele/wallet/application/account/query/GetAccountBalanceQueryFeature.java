package com.shebele.wallet.application.account.query;

import com.shebele.wallet.application.account.port.in.GetAccountBalanceQueryPort;
import com.shebele.wallet.application.port.out.AccountPersistencePort;
import com.shebele.wallet.domain.model.Account;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAccountBalanceQueryFeature implements GetAccountBalanceQueryPort {

    private final AccountPersistencePort accountPersistence;

    @Override
    public AccountBalanceResult execute(GetAccountBalanceQuery query) {
        log.info("Getting balance for: {}", query.getMsisdn());

        PhoneNumber phoneNumber = PhoneNumber.of(query.getMsisdn());
        Account account = accountPersistence.findByMsisdn(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + query.getMsisdn()));

        return AccountBalanceResult.builder()
                .msisdn(account.getMsisdn().getValue())
                .balance(account.getBalance().getAmount())
                .build();
    }
}