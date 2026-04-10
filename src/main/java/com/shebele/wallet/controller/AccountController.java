package com.shebele.wallet.controller;

import com.shebele.wallet.domain.Account;
import com.shebele.wallet.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        Account account = accountService.createAccount(request.msisdn, request.fullName);

        return ResponseEntity.ok(Map.of(
                "accountName",account.getAccountNumber(),
                "msisdn",account.getMsisdn(),
                "fullName",account.getFullName(),
                "balance", account.getBalance(),
                "status",account.getStatus()
        ));
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<?> getAccount(@PathVariable String msisdn) {
        Account account = accountService.getAccount(msisdn);
        return ResponseEntity.ok(Map.of(
                "msisdn", account.getMsisdn(),
                "fullName", account.getFullName(),
                "accountNumber", account.getAccountNumber(),
                "balance", account.getBalance(),
                "status", account.getStatus(),
                "createdAt", account.getCreatedAt()
        ));
    }

    @GetMapping("/{msisdn}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String msisdn){
        BigDecimal balance = accountService.getBalance(msisdn);

        return ResponseEntity.ok(Map.of(
                "msisdn", msisdn,
                "balance", balance,
                "timestamp",java.time.LocalTime.now()
        ));
    }

    @PostMapping("/{msisdn}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String msisdn,
                                     @Valid @RequestBody AmountRequest request) {
        Account account = accountService.updateBalance(msisdn, request.amount, true);
        return ResponseEntity.ok(Map.of(
                "msisdn", msisdn,
                "newBalance", account.getBalance(),
                "message", "Deposit successful"
        ));
    }
}

class RegisterRequest {
    @NotBlank
    @Pattern(regexp = "251[0-9]{9}")
    public String msisdn;

    @NotBlank @Size(min = 2, max = 100)
    public String fullName;
}

class AmountRequest {
    @NotNull
    @Positive
    public BigDecimal amount;
}
