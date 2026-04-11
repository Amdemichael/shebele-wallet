package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.ussd.USSDContext;
import com.shebele.wallet.ussd.USSDMenu;
import com.shebele.wallet.ussd.MenuHandler;
import com.shebele.wallet.ussd.UssdResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuyAirtimeHandler implements MenuHandler {

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        switch (input) {
            case "1":
                // Buy for my phone
                context.setRecipientMsisdn(context.getMsisdn());
                context.setCurrentMenu(USSDMenu.AIRTIME_AMOUNT);
                return responseBuilder.continueSession(
                        "Select amount:\n" +
                                "1. 10 Birr\n" +
                                "2. 25 Birr\n" +
                                "3. 50 Birr\n" +
                                "4. 100 Birr\n" +
                                "5. Custom amount\n" +
                                "0. Back"
                );

            case "2":
                // Buy for other number
                context.setCurrentMenu(USSDMenu.AIRTIME_PHONE);
                return responseBuilder.continueSession("Enter recipient phone number:");

            case "3":
                // Back to main menu
                context.setCurrentMenu(USSDMenu.MAIN);
                return responseBuilder.mainMenu();

            default:
                return responseBuilder.continueSession(
                        "Invalid selection.\n" +
                                "1. My phone\n" +
                                "2. Other number\n" +
                                "3. Back"
                );
        }
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.BUY_AIRTIME;
    }
}