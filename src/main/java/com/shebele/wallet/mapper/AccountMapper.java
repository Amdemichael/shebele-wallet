package com.shebele.wallet.mapper;

import com.shebele.wallet.domain.Account;
import com.shebele.wallet.dto.request.RegisterRequest;
import com.shebele.wallet.dto.response.AccountResponse;
import com.shebele.wallet.enums.AccountStatus;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }

        return AccountResponse.builder()
                .msisdn(account.getMsisdn())
                .fullName(account.getFullName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus().getDisplayName())  // Get display name from enum
                .createdAt(account.getCreatedAt())
                .build();
    }

    public Account toEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        Account account = new Account();
        account.setMsisdn(request.getMsisdn());
        account.setFullName(request.getFullName());
        account.setStatus(AccountStatus.ACTIVE);  // Set default status
        return account;
    }
}