package com.shebele.wallet.domain.model;

import com.shebele.wallet.domain.valueobject.AccountId;
import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import com.shebele.wallet.shared.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private AccountId id;
    private Long databaseId;
    private PhoneNumber msisdn;
    private String accountNumber;
    private String fullName;
    private Money balance;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Factory method for NEW accounts
    public static Account register(PhoneNumber msisdn, String accountNumber, String fullName) {
        Account account = new Account();
        account.setId(AccountId.generate());
        account.setMsisdn(msisdn);
        account.setAccountNumber(accountNumber);
        account.setFullName(fullName);
        account.setBalance(Money.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        return account;
    }

    // Business methods
    public void credit(Money amount) {
        this.balance = this.balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void debit(Money amount) {
        if (this.balance.isLessThan(amount)) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        this.balance = this.balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.status = AccountStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
}