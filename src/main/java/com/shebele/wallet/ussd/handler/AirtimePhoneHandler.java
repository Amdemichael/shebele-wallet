package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.helper.PhoneNumberHelper;
import com.shebele.wallet.ussd.USSDContext;
import com.shebele.wallet.ussd.USSDMenu;
import com.shebele.wallet.ussd.MenuHandler;
import com.shebele.wallet.ussd.UssdResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirtimePhoneHandler implements MenuHandler {

    private final PhoneNumberHelper phoneNumberHelper;

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        if (!phoneNumberHelper.isValidEthiopianPhone(input)) {
            return responseBuilder.continueSession(
                    "Invalid phone number.\nPlease enter a valid Ethiopian number (e.g., 251911111111):"
            );
        }

        context.setRecipientMsisdn(phoneNumberHelper.normalizeToStandard(input));
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
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.AIRTIME_PHONE;
    }
}