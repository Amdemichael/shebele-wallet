package com.shebele.wallet.application.transfer.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMoneyCommand {
    private String idempotencyKey;
    private String fromMsisdn;
    private String toMsisdn;
    private BigDecimal amount;
    private String description;
}