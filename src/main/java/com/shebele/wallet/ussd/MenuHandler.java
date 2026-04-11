package com.shebele.wallet.ussd;

import com.shebele.wallet.dto.response.UssdResponse;

public interface MenuHandler {
    UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder);
    USSDMenu getSupportedMenu();
}