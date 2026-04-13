package com.shebele.wallet.application.account.query;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

@Value
@Builder
public class DepositMoneyResult {
    String msisdn;
    BigDecimal newBalance;
    String transactionId;
}