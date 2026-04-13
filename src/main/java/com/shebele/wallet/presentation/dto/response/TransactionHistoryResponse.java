package com.shebele.wallet.presentation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionHistoryResponse {
    private String transactionRef;
    private String type;
    private String counterparty;  // ← ADD THIS FIELD
    private BigDecimal amount;
    private BigDecimal fee;
    private String description;
    private String status;
    private LocalDateTime timestamp;
}