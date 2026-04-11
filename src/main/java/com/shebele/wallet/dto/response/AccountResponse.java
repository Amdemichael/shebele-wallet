package com.shebele.wallet.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {
    private String msisdn;
    private String fullName;
    private String accountNumber;
    private BigDecimal balance;
    private String status;  // Will store display name
    private LocalDateTime createdAt;
}