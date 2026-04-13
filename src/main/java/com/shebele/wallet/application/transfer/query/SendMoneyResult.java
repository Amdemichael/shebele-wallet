package com.shebele.wallet.application.transfer.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMoneyResult {
    private String transactionId;
    private boolean success;
    private String errorMessage;
    private BigDecimal newBalance;
    private BigDecimal fee;
}