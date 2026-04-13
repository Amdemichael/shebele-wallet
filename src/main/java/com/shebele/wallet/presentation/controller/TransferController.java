package com.shebele.wallet.presentation.controller;

import com.shebele.wallet.application.transfer.command.SendMoneyCommand;
import com.shebele.wallet.application.transfer.port.in.GetTransactionHistoryQueryPort;
import com.shebele.wallet.application.transfer.port.in.SendMoneyCommandPort;
import com.shebele.wallet.application.transfer.query.GetTransactionHistoryQuery;
import com.shebele.wallet.application.transfer.query.SendMoneyResult;
import com.shebele.wallet.application.transfer.query.TransactionHistoryResult;
import com.shebele.wallet.presentation.dto.request.TransferMoneyRequest;
import com.shebele.wallet.presentation.dto.response.TransferMoneyResponse;
import com.shebele.wallet.presentation.dto.response.TransactionHistoryResponse;
import com.shebele.wallet.presentation.mapper.TransferPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Slf4j
public class TransferController {

    private final SendMoneyCommandPort sendMoneyCommand;
    private final GetTransactionHistoryQueryPort getTransactionHistoryQuery;
    private final TransferPresentationMapper mapper;

    @PostMapping
    public ResponseEntity<TransferMoneyResponse> transfer(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody TransferMoneyRequest request) {

        SendMoneyCommand command = mapper.toCommand(request, idempotencyKey);
        SendMoneyResult result = sendMoneyCommand.execute(command);

        if (result.isSuccess()) {
            return ResponseEntity.ok(mapper.toResponse(result));
        } else {
            return ResponseEntity.badRequest().body(mapper.toResponse(result));
        }
    }

    @GetMapping("/history/{msisdn}")
    public ResponseEntity<List<TransactionHistoryResponse>> getTransactionHistory(
            @PathVariable String msisdn,
            @RequestParam(defaultValue = "30") int days) {

        GetTransactionHistoryQuery query = GetTransactionHistoryQuery.builder()
                .msisdn(msisdn)
                .days(days)
                .build();

        List<TransactionHistoryResult> results = getTransactionHistoryQuery.execute(query);

        List<TransactionHistoryResponse> responses = results.stream()
                .map(mapper::toHistoryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Shebele Wallet is running!");
    }
}