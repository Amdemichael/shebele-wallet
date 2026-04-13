package com.shebele.wallet.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferMoneyResponse {
    private String transactionId;
    private boolean success;
    private String errorMessage;
    private BigDecimal newBalance;
    private BigDecimal fee;
    private LocalDateTime timestamp;
}