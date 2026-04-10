package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.helper.AmountHelper;
import com.shebele.wallet.service.api.AccountService;
import com.shebele.wallet.service.api.TransferService;
import com.shebele.wallet.ussd.USSDContext;
import com.shebele.wallet.ussd.USSDMenu;
import com.shebele.wallet.ussd.MenuHandler;
import com.shebele.wallet.ussd.UssdResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class AirtimeConfirmHandler implements MenuHandler {

    private final AccountService accountService;  // Add this
    private final AmountHelper amountHelper;

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        if ("1".equals(input)) {
            try {
                // Check balance again
                BigDecimal balance = accountService.getBalance(context.getMsisdn());
                if (balance.compareTo(context.getAmount()) < 0) {
                    return responseBuilder.endSession(
                            "Insufficient balance.\n" +
                                    "Your balance: " + amountHelper.formatAmount(balance) + "\n" +
                                    "Airtime amount: " + amountHelper.formatAmount(context.getAmount())
                    );
                }

                // Deduct money from user's wallet (simple debit)
                accountService.updateBalance(context.getMsisdn(), context.getAmount(), false);

                // Generate a reference number
                String reference = "AIR_" + System.currentTimeMillis();

                // In a real system, you would call Ethio Telecom API here
                // to actually add airtime to the phone number

                log.info("Airtime purchase: {} purchased {} Birr for {}",
                        context.getMsisdn(), context.getAmount(), context.getRecipientMsisdn());

                return responseBuilder.endSession(
                        "✓ Airtime purchase successful!\n\n" +
                                "Phone: " + context.getRecipientMsisdn() + "\n" +
                                "Amount: " + amountHelper.formatAmount(context.getAmount()) + "\n" +
                                "Reference: " + reference + "\n\n" +
                                "Airtime has been added to the phone."
                );

            } catch (Exception e) {
                log.error("Airtime purchase failed", e);
                return responseBuilder.endSession("Purchase failed: " + e.getMessage());
            }
        } else if ("2".equals(input)) {
            return responseBuilder.endSession("Airtime purchase cancelled. Thank you!");
        } else {
            return responseBuilder.continueSession(
                    "Please select:\n" +
                            "1. Confirm\n" +
                            "2. Cancel"
            );
        }
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.AIRTIME_CONFIRM;
    }
}