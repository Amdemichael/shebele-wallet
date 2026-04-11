package com.shebele.wallet.ussd;

import com.shebele.wallet.dto.response.UssdResponse;
import org.springframework.stereotype.Component;

@Component
public class UssdResponseBuilder {

    private static final String MAIN_MENU =
            "Welcome to Shebele Wallet\n" +
                    "1. Send Money\n" +
                    "2. Check Balance\n" +
                    "3. My Account\n" +
                    "4. Exit\n" +
                    "5. Buy Airtime";

    public UssdResponse continueSession(String message) {
        return UssdResponse.builder()
                .message(message)
                .endSession(false)
                .build();
    }

    public UssdResponse endSession(String message) {
        return UssdResponse.builder()
                .message(message)
                .endSession(true)
                .build();
    }

    public UssdResponse mainMenu() {
        return continueSession(MAIN_MENU);
    }

    public String formatMainMenu() {
        return MAIN_MENU;
    }
}