package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.helper.AmountHelper;
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
public class AirtimeAmountHandler implements MenuHandler {

    private final AccountService accountService;
    private final AmountHelper amountHelper;

    // Predefined airtime amounts
    private static final BigDecimal AMOUNT_10 = new BigDecimal("10");
    private static final BigDecimal AMOUNT_25 = new BigDecimal("25");
    private static final BigDecimal AMOUNT_50 = new BigDecimal("50");
    private static final BigDecimal AMOUNT_100 = new BigDecimal("100");

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        // Handle back option
        if ("0".equals(input)) {
            context.setCurrentMenu(USSDMenu.BUY_AIRTIME);
            return responseBuilder.continueSession(
                    "Buy Airtime\n" +
                            "1. My phone\n" +
                            "2. Other number\n" +
                            "3. Back"
            );
        }

        BigDecimal amount = null;

        switch (input) {
            case "1":
                amount = AMOUNT_10;
                break;
            case "2":
                amount = AMOUNT_25;
                break;
            case "3":
                amount = AMOUNT_50;
                break;
            case "4":
                amount = AMOUNT_100;
                break;
            case "5":
                // Custom amount - stay in same menu but prompt for input
                return responseBuilder.continueSession("Enter custom amount in Birr:");
            default:
                // Try parsing as custom amount
                try {
                    amount = new BigDecimal(input);
                    if (!amountHelper.isValidAmount(amount)) {
                        return responseBuilder.continueSession(
                                "Amount must be between " + amountHelper.getMinimumTransfer() +
                                        " and " + amountHelper.getMaximumTransfer() + " Birr.\n" +
                                        "Please enter a valid amount:"
                        );
                    }
                } catch (NumberFormatException e) {
                    return responseBuilder.continueSession(
                            "Invalid selection.\n" +
                                    "1. 10 Birr\n" +
                                    "2. 25 Birr\n" +
                                    "3. 50 Birr\n" +
                                    "4. 100 Birr\n" +
                                    "5. Custom amount\n" +
                                    "0. Back"
                    );
                }
        }

        // Check if user has sufficient balance
        try {
            BigDecimal balance = accountService.getBalance(context.getMsisdn());
            if (balance.compareTo(amount) < 0) {
                return responseBuilder.continueSession(
                        "Insufficient balance.\n" +
                                "Your balance: " + amountHelper.formatAmount(balance) + "\n" +
                                "Airtime amount: " + amountHelper.formatAmount(amount) + "\n\n" +
                                "Please select a lower amount:"
                );
            }
        } catch (Exception e) {
            return responseBuilder.endSession("Account not found. Please register first.");
        }

        context.setAmount(amount);
        context.setCurrentMenu(USSDMenu.AIRTIME_CONFIRM);

        return responseBuilder.continueSession(
                "Confirm airtime purchase:\n" +
                        "Phone: " + context.getRecipientMsisdn() + "\n" +
                        "Amount: " + amountHelper.formatAmount(amount) + "\n\n" +
                        "1. Confirm\n" +
                        "2. Cancel"
        );
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.AIRTIME_AMOUNT;
    }
}