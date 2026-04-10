package com.shebele.wallet.controller;

import com.shebele.wallet.domain.Transaction;
import com.shebele.wallet.service.TransferService;
import com.shebele.wallet.service.TransferResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
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
                request.fromMsisdn,
                request.toMsisdn,
                request.amount,
                request.description
        );

        if (result.isDuplicate()) {
            // Return 409 Conflict for duplicate requests
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
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "transactionRef", transaction.getTransactionRef(),
                "fromAccount", transaction.getFromMsisdn(),
                "toAccount", transaction.getToMsisdn(),
                "amount", transaction.getAmount(),
                "timestamp", transaction.getCreatedAt()
        ));
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

class TransferRequest {
    @NotBlank @Pattern(regexp = "251[0-9]{9}")
    public String fromMsisdn;

    @NotBlank @Pattern(regexp = "251[0-9]{9}")
    public String toMsisdn;

    @NotNull @Positive
    public BigDecimal amount;

    @Size(max = 255)
    public String description;
}