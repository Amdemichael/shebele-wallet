package com.shebele.wallet.presentation.controller;

import com.shebele.wallet.application.account.command.DepositMoneyCommand;
import com.shebele.wallet.application.account.command.RegisterAccountCommand;
import com.shebele.wallet.application.account.port.in.DepositMoneyCommandPort;
import com.shebele.wallet.application.account.port.in.GetAccountBalanceQueryPort;
import com.shebele.wallet.application.account.port.in.GetAccountDetailsQueryPort;
import com.shebele.wallet.application.account.port.in.RegisterAccountCommandPort;
import com.shebele.wallet.application.account.query.AccountBalanceResult;
import com.shebele.wallet.application.account.query.AccountDetailsResult;
import com.shebele.wallet.application.account.query.DepositMoneyResult;
import com.shebele.wallet.application.account.query.GetAccountBalanceQuery;
import com.shebele.wallet.application.account.query.GetAccountDetailsQuery;
import com.shebele.wallet.presentation.dto.request.DepositMoneyRequest;
import com.shebele.wallet.presentation.dto.request.RegisterAccountRequest;
import com.shebele.wallet.presentation.dto.response.AccountBalanceResponse;
import com.shebele.wallet.presentation.dto.response.AccountDetailsResponse;
import com.shebele.wallet.presentation.dto.response.DepositMoneyResponse;
import com.shebele.wallet.presentation.mapper.AccountPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final RegisterAccountCommandPort registerAccountCommand;
    private final DepositMoneyCommandPort depositMoneyCommand;
    private final GetAccountBalanceQueryPort getAccountBalanceQuery;
    private final GetAccountDetailsQueryPort getAccountDetailsQuery;
    private final AccountPresentationMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<AccountDetailsResponse> register(@Valid @RequestBody RegisterAccountRequest request) {
        RegisterAccountCommand command = mapper.toCommand(request);
        AccountDetailsResult result = registerAccountCommand.execute(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/{msisdn}")
    public ResponseEntity<AccountDetailsResponse> getAccountDetails(@PathVariable String msisdn) {
        GetAccountDetailsQuery query = GetAccountDetailsQuery.builder().msisdn(msisdn).build();
        AccountDetailsResult result = getAccountDetailsQuery.execute(query);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/{msisdn}/balance")
    public ResponseEntity<AccountBalanceResponse> getBalance(@PathVariable String msisdn) {
        GetAccountBalanceQuery query = GetAccountBalanceQuery.builder().msisdn(msisdn).build();
        AccountBalanceResult result = getAccountBalanceQuery.execute(query);
        return ResponseEntity.ok(mapper.toBalanceResponse(result));
    }

    @PostMapping("/{msisdn}/deposit")
    public ResponseEntity<DepositMoneyResponse> deposit(
            @PathVariable String msisdn,
            @Valid @RequestBody DepositMoneyRequest request) {
        DepositMoneyCommand command = mapper.toCommand(msisdn, request);
        DepositMoneyResult result = depositMoneyCommand.execute(command);
        return ResponseEntity.ok(mapper.toDepositResponse(result));
    }
}