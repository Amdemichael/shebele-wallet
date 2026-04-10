package com.shebele.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BalanceResponse {
    private String msisdn;
    private BigDecimal balance;
    private LocalDateTime timestamp;
}