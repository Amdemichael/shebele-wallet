package com.shebele.wallet.presentation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class DepositMoneyResponse {
    private String msisdn;
    private BigDecimal newBalance;
    private String transactionId;
    private String message;
    private LocalDateTime timestamp;
}