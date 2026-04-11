package com.shebele.wallet.ussd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum USSDMenu {

    MAIN("MAIN", "mainMenu"),
    SEND_PHONE("SEND_PHONE", "sendPhone"),
    SEND_AMOUNT("SEND_AMOUNT", "sendAmount"),
    SEND_CONFIRM("SEND_CONFIRM", "sendConfirm"),
    CHECK_BALANCE("CHECK_BALANCE", "checkBalance"),
    MY_ACCOUNT("MY_ACCOUNT", "myAccount"),
    EXIT("EXIT", "exit"),

    // NEW: Buy Airtime menus
    BUY_AIRTIME("BUY_AIRTIME", "buyAirtime"),           // Main airtime menu
    AIRTIME_PHONE("AIRTIME_PHONE", "airtimePhone"),     // Enter phone number
    AIRTIME_AMOUNT("AIRTIME_AMOUNT", "airtimeAmount"),  // Select amount
    AIRTIME_CONFIRM("AIRTIME_CONFIRM", "airtimeConfirm"); // Confirm purchase

    private final String code;
    private final String handler;

    public static USSDMenu fromCode(String code) {
        for (USSDMenu menu : values()) {
            if (menu.code.equals(code)) {
                return menu;
            }
        }
        return MAIN;
    }
}