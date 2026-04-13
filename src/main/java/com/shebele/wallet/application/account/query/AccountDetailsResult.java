package com.shebele.wallet.application.account.query;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class AccountDetailsResult {
    String msisdn;
    String fullName;
    String accountNumber;
    BigDecimal balance;
    String status;
    LocalDateTime createdAt;
}