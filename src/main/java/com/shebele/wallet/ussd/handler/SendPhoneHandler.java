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
public class SendPhoneHandler implements MenuHandler {

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
        context.setCurrentMenu(USSDMenu.SEND_AMOUNT);

        return responseBuilder.continueSession("Enter amount in Birr:");
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.SEND_PHONE;
    }
}