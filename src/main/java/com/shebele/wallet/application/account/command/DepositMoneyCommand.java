package com.shebele.wallet.application.account.command;

import lombok.Builder;
import lombok.Value;
import java.math.BigDecimal;

@Value
@Builder
public class DepositMoneyCommand {
    String msisdn;
    BigDecimal amount;
    String reference;
}