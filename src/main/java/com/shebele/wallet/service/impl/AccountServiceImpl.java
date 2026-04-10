package com.shebele.wallet.service.impl;
import com.shebele.wallet.domain.Account;
import com.shebele.wallet.repository.AccountRepository;
import com.shebele.wallet.service.api.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(String msisdn, String fullName) {
        if (accountRepository.existsByMsisdn(msisdn)) {
            throw new RuntimeException("Account already exists: " + msisdn);
        }

        Account account = new Account();
        account.setMsisdn(msisdn);
        account.setFullName(fullName);
        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        Account saved = accountRepository.save(account);
        log.info("Created account: {} for {}", saved.getAccountNumber(), msisdn);
        return saved;
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(String msisdn) {
        Account account = accountRepository.findByMsisdn(msisdn)
                .orElseThrow(() -> new RuntimeException("Account not found: " + msisdn));
        return account.getBalance();
    }

    @Transactional
    public Account updateBalance(String msisdn, BigDecimal amount, boolean isCredit) {
        Account account = accountRepository.findByMsisdnWithLock(msisdn)
                .orElseThrow(() -> new RuntimeException("Account not found: " + msisdn));

        if (isCredit) {
            account.setBalance(account.getBalance().add(amount));
            log.debug("Credited {} to {}", amount, msisdn);
        } else {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance. Available: " + account.getBalance());
            }
            account.setBalance(account.getBalance().subtract(amount));
            log.debug("Debited {} from {}", amount, msisdn);
        }

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccount(String msisdn) {
        return accountRepository.findByMsisdn(msisdn)
                .orElseThrow(() -> new RuntimeException("Account not found: " + msisdn));
    }

    private String generateAccountNumber() {
        return "SHE" + System.currentTimeMillis();
    }
}