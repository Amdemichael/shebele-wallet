package com.shebele.wallet.ussd;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class USSDContext {
    private String sessionId;
    private String msisdn;
    private String userInput;
    private String serviceCode;
    private USSDMenu currentMenu;
    private String recipientMsisdn;
    private BigDecimal amount;

    public boolean isFirstRequest() {
        return userInput == null || userInput.trim().isEmpty();
    }

    public boolean hasRecipient() {
        return recipientMsisdn != null && !recipientMsisdn.isEmpty();
    }

    public boolean hasAmount() {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}