package com.shebele.wallet.application.transfer.query;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class TransactionHistoryResult {
    String transactionRef;
    String type;
    String counterparty;  // ← ADD THIS FIELD
    BigDecimal amount;
    BigDecimal fee;
    String description;
    String status;
    LocalDateTime timestamp;
}