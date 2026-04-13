package com.shebele.wallet.application.account.query;

import com.shebele.wallet.application.account.port.in.GetAccountDetailsQueryPort;
import com.shebele.wallet.application.port.out.AccountPersistencePort;
import com.shebele.wallet.domain.model.Account;
import com.shebele.wallet.domain.model.TransactionEntry;
import com.shebele.wallet.domain.repository.TransactionEntryRepository;
import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetAccountDetailsQueryFeature implements GetAccountDetailsQueryPort {

    private final AccountPersistencePort accountPersistence;
    private final TransactionEntryRepository transactionEntryRepository;

    @Override
    public AccountDetailsResult execute(GetAccountDetailsQuery query) {
        log.info("Getting account details for: {}", query.getMsisdn());

        PhoneNumber phoneNumber = PhoneNumber.of(query.getMsisdn());
        Account account = accountPersistence.findByMsisdn(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + query.getMsisdn()));

        // Calculate balance from transaction entries
        List<TransactionEntry> entries = transactionEntryRepository
                .findByAccountMsisdnOrderByCreatedAtDesc(query.getMsisdn());

        Money balance = Money.ZERO;
        for (TransactionEntry entry : entries) {
            if (entry.isCredit()) {
                balance = balance.add(entry.getAmount());
            } else {
                balance = balance.subtract(entry.getAmount());
            }
        }

        return AccountDetailsResult.builder()
                .msisdn(account.getMsisdn().getValue())
                .fullName(account.getFullName())
                .accountNumber(account.getAccountNumber())
                .balance(balance.getAmount())
                .status(account.getStatus().name())
                .createdAt(account.getCreatedAt())
                .build();
    }
}