package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.service.api.AccountService;
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
public class MainMenuHandler implements MenuHandler {

    private final AccountService accountService;

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        switch (input) {
            case "1":
                context.setCurrentMenu(USSDMenu.SEND_PHONE);
                return responseBuilder.continueSession("Enter recipient phone number:");

            case "2":
                try {
                    BigDecimal balance = accountService.getBalance(context.getMsisdn());
                    return responseBuilder.endSession("Your balance is: " + balance + " Birr");
                } catch (Exception e) {
                    return responseBuilder.endSession("Account not found. Please register first.");
                }

            case "3":
                try {
                    var account = accountService.getAccount(context.getMsisdn());
                    return responseBuilder.endSession(
                            "Name: " + account.getFullName() + "\n" +
                                    "Account: " + account.getAccountNumber() + "\n" +
                                    "Balance: " + account.getBalance() + " Birr"
                    );
                } catch (Exception e) {
                    return responseBuilder.endSession("Account not found. Please register first.");
                }

            case "4":
                return responseBuilder.endSession("Thank you for using Shebele Wallet. Goodbye!");


            case "5":  // NEW: Buy Airtime
                context.setCurrentMenu(USSDMenu.BUY_AIRTIME);
                return responseBuilder.continueSession(
                        "Buy Airtime\n" +
                                "1. My phone\n" +
                                "2. Other number\n" +
                                "3. Back"
                );

            default:
                return responseBuilder.continueSession(
                        "Invalid selection. Please try again:\n" +
                                "1. Send Money\n" +
                                "2. Check Balance\n" +
                                "3. My Account\n" +
                                "4. Exit\n" +
                                "5. Buy Airtime"
                );
        }
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.MAIN;
    }
}