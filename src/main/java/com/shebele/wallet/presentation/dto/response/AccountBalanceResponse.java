package com.shebele.wallet.presentation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountBalanceResponse {
    private String msisdn;
    private BigDecimal balance;
    private LocalDateTime timestamp;
}