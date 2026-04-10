package com.shebele.wallet.controller;

import com.shebele.wallet.domain.Account;
import com.shebele.wallet.dto.request.DepositRequest;
import com.shebele.wallet.dto.request.RegisterRequest;
import com.shebele.wallet.dto.response.AccountResponse;
import com.shebele.wallet.dto.response.BalanceResponse;
import com.shebele.wallet.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImpl accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody RegisterRequest request) {
        Account account = accountService.createAccount(request.getMsisdn(), request.getFullName());

        AccountResponse response = AccountResponse.builder()
                .msisdn(account.getMsisdn())
                .fullName(account.getFullName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .createdAt(account.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String msisdn) {
        Account account = accountService.getAccount(msisdn);

        AccountResponse response = AccountResponse.builder()
                .msisdn(account.getMsisdn())
                .fullName(account.getFullName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .status(account.getStatus().name())
                .createdAt(account.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{msisdn}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String msisdn) {
        BigDecimal balance = accountService.getBalance(msisdn);

        BalanceResponse response = BalanceResponse.builder()
                .msisdn(msisdn)
                .balance(balance)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{msisdn}/deposit")
    public ResponseEntity<BalanceResponse> deposit(@PathVariable String msisdn,
                                                   @Valid @RequestBody DepositRequest request) {
        Account account = accountService.updateBalance(msisdn, request.getAmount(), true);

        BalanceResponse response = BalanceResponse.builder()
                .msisdn(msisdn)
                .balance(account.getBalance())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}