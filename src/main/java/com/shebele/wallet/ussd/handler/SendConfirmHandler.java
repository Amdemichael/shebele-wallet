package com.shebele.wallet.ussd.handler;

import com.shebele.wallet.dto.response.TransferResult;
import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.service.api.TransferService;
import com.shebele.wallet.ussd.USSDContext;
import com.shebele.wallet.ussd.USSDMenu;
import com.shebele.wallet.ussd.MenuHandler;
import com.shebele.wallet.ussd.UssdResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendConfirmHandler implements MenuHandler {

    private final TransferService transferService;

    @Override
    public UssdResponse handle(USSDContext context, UssdResponseBuilder responseBuilder) {
        String input = context.getUserInput();

        if ("1".equals(input)) {
            try {
                String idempotencyKey = "USSD_" + context.getSessionId() + "_" +
                        context.getMsisdn() + "_" + System.currentTimeMillis();

                TransferResult result = transferService.transfer(
                        idempotencyKey,
                        context.getMsisdn(),
                        context.getRecipientMsisdn(),
                        context.getAmount(),
                        "USSD Transfer"
                );

                if (result.isSuccess()) {
                    return responseBuilder.endSession(
                            "Transfer successful!\n" +
                                    "Amount: " + context.getAmount() + " Birr\n" +
                                    "To: " + context.getRecipientMsisdn() + "\n" +
                                    "Reference: " + result.getTransactionRef()
                    );
                } else {
                    return responseBuilder.endSession("Transfer failed: " + result.getErrorMessage());
                }
            } catch (Exception e) {
                log.error("Transfer failed", e);
                return responseBuilder.endSession("Transfer failed: " + e.getMessage());
            }
        } else if ("2".equals(input)) {
            return responseBuilder.endSession("Transfer cancelled. Thank you!");
        } else {
            return responseBuilder.continueSession("Please select:\n1. Confirm\n2. Cancel");
        }
    }

    @Override
    public USSDMenu getSupportedMenu() {
        return USSDMenu.SEND_CONFIRM;
    }
}