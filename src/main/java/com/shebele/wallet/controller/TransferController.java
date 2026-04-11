package com.shebele.wallet.controller;

import com.shebele.wallet.domain.Transaction;
import com.shebele.wallet.dto.request.TransferRequest;
import com.shebele.wallet.dto.response.TransferResponse;
import com.shebele.wallet.dto.response.TransferResult;
import com.shebele.wallet.service.api.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> transfer(
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody TransferRequest request) {

        TransferResult result = transferService.transfer(
                idempotencyKey,
                request.getFromMsisdn(),
                request.getToMsisdn(),
                request.getAmount(),
                request.getDescription()
        );

        if (result.isDuplicate()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", "DUPLICATE",
                            "message", "This request has already been processed",
                            "transactionRef", result.getTransactionRef(),
                            "processedAt", result.getTransaction().getCreatedAt()
                    ));
        }

        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", "FAILED",
                            "message", result.getErrorMessage()
                    ));
        }

        Transaction transaction = result.getTransaction();
        TransferResponse response = TransferResponse.builder()
                .transactionRef(transaction.getTransactionRef())
                .status(transaction.getStatus().name())
                .amount(transaction.getAmount())
                .fromAccount(transaction.getFromMsisdn())
                .toAccount(transaction.getToMsisdn())
                .timestamp(transaction.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{msisdn}")
    public ResponseEntity<?> getHistory(@PathVariable String msisdn,
                                        @RequestParam(defaultValue = "30") int days) {
        List<Transaction> transactions = transferService.getTransactionHistory(msisdn, days);

        List<Map<String, Object>> history = transactions.stream()
                .map(t -> Map.<String, Object>of(
                        "reference", t.getTransactionRef(),
                        "type", t.getFromMsisdn().equals(msisdn) ? "SENT" : "RECEIVED",
                        "counterparty", t.getFromMsisdn().equals(msisdn) ? t.getToMsisdn() : t.getFromMsisdn(),
                        "amount", t.getAmount(),
                        "description", t.getDescription() != null ? t.getDescription() : "",
                        "status", t.getStatus(),
                        "date", t.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
                "msisdn", msisdn,
                "transactions", history,
                "count", history.size()
        ));
    }
}