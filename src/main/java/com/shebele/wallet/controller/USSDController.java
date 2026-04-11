package com.shebele.wallet.controller;

import com.shebele.wallet.domain.UssdSession;
import com.shebele.wallet.dto.request.UssdRequest;
import com.shebele.wallet.dto.response.UssdResponse;
import com.shebele.wallet.service.api.UssdSessionService;
import com.shebele.wallet.ussd.USSDContext;
import com.shebele.wallet.ussd.USSDMenu;
import com.shebele.wallet.ussd.USSDMenuRouter;
import com.shebele.wallet.ussd.UssdResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ussd")
@RequiredArgsConstructor
@Slf4j
public class USSDController {

    private final UssdSessionService sessionService;
    private final USSDMenuRouter menuRouter;
    private final UssdResponseBuilder responseBuilder;

    @PostMapping("/callback")
    public UssdResponse handleUssdRequest(@RequestBody UssdRequest request) {

        String sessionId = request.getSessionId();
        String msisdn = request.getMsisdn();
        String userInput = request.getUserInput();

        log.info("USSD Request - Session: {}, MSISDN: {}, Input: '{}'", sessionId, msisdn, userInput);

        // Get or create session
        UssdSession session = sessionService.getOrCreateSession(sessionId, msisdn);

        // Build context
        USSDContext context = USSDContext.builder()
                .sessionId(sessionId)
                .msisdn(msisdn)
                .userInput(userInput)
                .serviceCode(request.getServiceCode())
                .currentMenu(USSDMenu.fromCode(session.getCurrentMenu()))
                .recipientMsisdn(session.getRecipientMsisdn())
                .amount(session.getAmount())
                .build();

        // Handle first request or empty input
        if (context.isFirstRequest()) {
            if (context.getCurrentMenu() != USSDMenu.MAIN) {
                return continueWithCurrentMenu(session, context);
            }
            return responseBuilder.mainMenu();
        }

        // Route to appropriate handler
        UssdResponse response = menuRouter.route(context);

        // Update session state
        updateSessionFromContext(session, context);

        // Clean up if session ended
        if (response.isEndSession()) {
            sessionService.endSession(sessionId);
        } else {
            sessionService.updateSession(session);
        }

        return response;
    }

    private UssdResponse continueWithCurrentMenu(UssdSession session, USSDContext context) {
        String prompt = getPromptForMenu(context.getCurrentMenu(), context);
        return responseBuilder.continueSession(prompt);
    }

    private void updateSessionFromContext(UssdSession session, USSDContext context) {
        session.setCurrentMenu(context.getCurrentMenu().getCode());
        session.setRecipientMsisdn(context.getRecipientMsisdn());
        session.setAmount(context.getAmount());
    }

    private String getPromptForMenu(USSDMenu menu, USSDContext context) {
        switch (menu) {
            case SEND_PHONE:
                return "Enter recipient phone number:";
            case SEND_AMOUNT:
                return "Enter amount in Birr:";
            case SEND_CONFIRM:
                return "Confirm transfer:\nTo: " + context.getRecipientMsisdn() +
                        "\nAmount: " + context.getAmount() + " Birr\n1. Confirm\n2. Cancel";
            default:
                return "Welcome to Shebele Wallet\n1. Send Money\n2. Check Balance\n3. My Account\n4. Exit";
        }
    }
}