package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.helper.AmountHelper;
import com.shebele.wallet.helper.PhoneNumberHelper;
import com.shebele.wallet.service.api.AccountService;
import com.shebele.wallet.ussd.USSDContext;
import com.shebele.wallet.ussd.USSDMenu;
import com.shebele.wallet.ussd.MenuHandler;
import com.shebele.wallet.ussd.UssdResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SendAmountHandler implements MenuHandler {

    private final AccountService accountService;
    private final AmountHelper amountHelper;
    private final PhoneNumberHelper phoneNumberHelper;

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        try {
            BigDecimal amount = new BigDecimal(input);

            if (!amountHelper.isValidAmount(amount)) {
                return responseBuilder.continueSession(
                        "Amount must be between " + amountHelper.getMinimumTransfer() +
                                " and " + amountHelper.getMaximumTransfer() + " Birr:"
                );
            }

            BigDecimal balance = accountService.getBalance(context.getMsisdn());
            if (balance.compareTo(amount) < 0) {
                return responseBuilder.continueSession(
                        "Insufficient balance.\nYour balance is: " +
                                amountHelper.formatAmount(balance) + "\nEnter a lower amount:"
                );
            }

            context.setAmount(amount);
            context.setCurrentMenu(USSDMenu.SEND_CONFIRM);

            return responseBuilder.continueSession(
                    "Confirm transfer:\n" +
                            "To: " + phoneNumberHelper.maskPhoneNumber(context.getRecipientMsisdn()) + "\n" +
                            "Amount: " + amountHelper.formatAmount(amount) + "\n" +
                            "1. Confirm\n" +
                            "2. Cancel"
            );
        } catch (NumberFormatException e) {
            return responseBuilder.continueSession("Invalid amount.\nEnter numbers only (e.g., 100):");
        }
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.SEND_AMOUNT;
    }
}