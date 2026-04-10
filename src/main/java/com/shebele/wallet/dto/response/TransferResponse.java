package com.shebele.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponse {
    private String transactionRef;
    private String status;  // Will store display name
    private BigDecimal amount;
    private String fromAccount;
    private String toAccount;
    private LocalDateTime timestamp;
}