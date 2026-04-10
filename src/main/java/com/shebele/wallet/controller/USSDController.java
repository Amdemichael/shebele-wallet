package com.shebele.wallet.controller;

import com.shebele.wallet.service.AccountService;
import com.shebele.wallet.service.TransferService;
import com.shebele.wallet.service.TransferResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/ussd")
@RequiredArgsConstructor
@Slf4j
public class USSDController {

    private final AccountService accountService;
    private final TransferService transferService;

    // Session storage - in production use Redis
    private final Map<String, UssdSession> sessions = new ConcurrentHashMap<>();

    @PostMapping("/callback")
    public UssdResponse handleUssdRequest(@RequestBody UssdRequest request) {

        String sessionId = request.getSessionId();
        String msisdn = request.getMsisdn();
        String userInput = request.getUserInput();

        log.info("USSD Request - Session: {}, MSISDN: {}, Input: '{}'", sessionId, msisdn, userInput);

        // Get or create session
        UssdSession session = sessions.get(sessionId);

        // First request or session expired - create new
        if (session == null) {
            log.info("Creating new session for: {}", sessionId);
            session = new UssdSession(sessionId, msisdn);
            sessions.put(sessionId, session);
        }

        // Handle empty input (user just pressed Send without typing)
        if (userInput == null || userInput.trim().isEmpty()) {
            // If we're in the middle of a flow, show current prompt
            if (!"MAIN".equals(session.getCurrentMenu())) {
                String prompt = getPromptForMenu(session.getCurrentMenu(), session);
                return UssdResponse.continueSession(prompt);
            }
            // Otherwise show main menu
            return showMainMenu(session);
        }

        // Process based on current menu
        String currentMenu = session.getCurrentMenu();
        log.info("Current menu: {}", currentMenu);

        switch (currentMenu) {
            case "MAIN":
                return handleMainMenu(session, userInput);
            case "SEND_PHONE":
                return handleSendPhone(session, userInput);
            case "SEND_AMOUNT":
                return handleSendAmount(session, userInput);
            case "SEND_CONFIRM":
                return handleSendConfirm(session, userInput);
            default:
                log.warn("Unknown menu: {}, resetting session", currentMenu);
                session.setCurrentMenu("MAIN");
                sessions.put(sessionId, session);
                return showMainMenu(session);
        }
    }

    private UssdResponse showMainMenu(UssdSession session) {
        session.setCurrentMenu("MAIN");
        session.setRecipientMsisdn(null);
        session.setAmount(null);
        sessions.put(session.getSessionId(), session);

        return UssdResponse.continueSession(
                "Welcome to Shebele Wallet\n" +
                        "1. Send Money\n" +
                        "2. Check Balance\n" +
                        "3. My Account\n" +
                        "4. Exit"
        );
    }

    private String getPromptForMenu(String menu, UssdSession session) {
        switch (menu) {
            case "SEND_PHONE":
                return "Enter recipient phone number:";
            case "SEND_AMOUNT":
                return "Enter amount in Birr:";
            case "SEND_CONFIRM":
                return "Confirm transfer:\nTo: " + session.getRecipientMsisdn() +
                        "\nAmount: " + session.getAmount() + " Birr\n1. Confirm\n2. Cancel";
            default:
                return "Welcome to Shebele Wallet\n1. Send Money\n2. Check Balance\n3. My Account\n4. Exit";
        }
    }

    private UssdResponse handleMainMenu(UssdSession session, String input) {
        log.info("Handling main menu with input: {}", input);

        switch (input) {
            case "1":
                session.setCurrentMenu("SEND_PHONE");
                session.setRecipientMsisdn(null);
                session.setAmount(null);
                sessions.put(session.getSessionId(), session);
                return UssdResponse.continueSession("Enter recipient phone number:");

            case "2":
                try {
                    BigDecimal balance = accountService.getBalance(session.getMsisdn());
                    sessions.remove(session.getSessionId()); // End session
                    return UssdResponse.endSession("Your balance is: " + balance + " Birr");
                } catch (Exception e) {
                    sessions.remove(session.getSessionId());
                    return UssdResponse.endSession("Account not found. Please register first.");
                }

            case "3":
                try {
                    var account = accountService.getAccount(session.getMsisdn());
                    sessions.remove(session.getSessionId());
                    return UssdResponse.endSession(
                            "Name: " + account.getFullName() + "\n" +
                                    "Account: " + account.getAccountNumber() + "\n" +
                                    "Balance: " + account.getBalance() + " Birr"
                    );
                } catch (Exception e) {
                    sessions.remove(session.getSessionId());
                    return UssdResponse.endSession("Account not found. Please register first.");
                }

            case "4":
                sessions.remove(session.getSessionId());
                return UssdResponse.endSession("Thank you for using Shebele Wallet. Goodbye!");

            default:
                return UssdResponse.continueSession(
                        "Invalid selection. Please try again:\n" +
                                "1. Send Money\n2. Check Balance\n3. My Account\n4. Exit"
                );
        }
    }

    private UssdResponse handleSendPhone(UssdSession session, String input) {
        log.info("Handling send phone with input: {}", input);

        // Validate Ethiopian phone number
        if (!input.matches("251[0-9]{9}")) {
            return UssdResponse.continueSession(
                    "Invalid phone number.\nPlease enter a valid Ethiopian number (e.g., 251911111111):"
            );
        }

        session.setRecipientMsisdn(input);
        session.setCurrentMenu("SEND_AMOUNT");
        sessions.put(session.getSessionId(), session);

        return UssdResponse.continueSession("Enter amount in Birr:");
    }

    private UssdResponse handleSendAmount(UssdSession session, String input) {
        log.info("Handling send amount with input: {}", input);

        try {
            BigDecimal amount = new BigDecimal(input);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return UssdResponse.continueSession("Amount must be positive.\nEnter amount in Birr:");
            }

            // Check if user has sufficient balance
            BigDecimal balance = accountService.getBalance(session.getMsisdn());
            if (balance.compareTo(amount) < 0) {
                return UssdResponse.continueSession(
                        "Insufficient balance.\nYour balance is: " + balance + " Birr\n" +
                                "Enter a lower amount:"
                );
            }

            session.setAmount(amount);
            session.setCurrentMenu("SEND_CONFIRM");
            sessions.put(session.getSessionId(), session);

            return UssdResponse.continueSession(
                    "Confirm transfer:\n" +
                            "To: " + session.getRecipientMsisdn() + "\n" +
                            "Amount: " + amount + " Birr\n" +
                            "1. Confirm\n" +
                            "2. Cancel"
            );
        } catch (NumberFormatException e) {
            return UssdResponse.continueSession("Invalid amount.\nEnter numbers only (e.g., 100):");
        }
    }

    private UssdResponse handleSendConfirm(UssdSession session, String input) {
        log.info("Handling send confirm with input: {}", input);

        if ("1".equals(input)) {
            try {
                String idempotencyKey = "USSD_" + session.getSessionId() + "_" + System.currentTimeMillis();

                TransferResult result = transferService.transfer(
                        idempotencyKey,
                        session.getMsisdn(),
                        session.getRecipientMsisdn(),
                        session.getAmount(),
                        "USSD Transfer"
                );

                sessions.remove(session.getSessionId());

                if (result.isSuccess()) {
                    return UssdResponse.endSession(
                            "Transfer successful!\n" +
                                    "Amount: " + session.getAmount() + " Birr\n" +
                                    "To: " + session.getRecipientMsisdn() + "\n" +
                                    "Reference: " + result.getTransactionRef()
                    );
                } else {
                    return UssdResponse.endSession("Transfer failed: " + result.getErrorMessage());
                }
            } catch (Exception e) {
                log.error("Transfer failed", e);
                sessions.remove(session.getSessionId());
                return UssdResponse.endSession("Transfer failed: " + e.getMessage());
            }
        } else if ("2".equals(input)) {
            sessions.remove(session.getSessionId());
            return UssdResponse.endSession("Transfer cancelled. Thank you!");
        } else {
            return UssdResponse.continueSession(
                    "Please select:\n" +
                            "1. Confirm\n" +
                            "2. Cancel"
            );
        }
    }
}

// USSD Session class
class UssdSession {
    private String sessionId;
    private String msisdn;
    private String currentMenu;
    private String recipientMsisdn;
    private BigDecimal amount;
    private long createdAt;
    private long lastUpdatedAt;

    public UssdSession(String sessionId, String msisdn) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.currentMenu = "MAIN";
        this.createdAt = System.currentTimeMillis();
        this.lastUpdatedAt = System.currentTimeMillis();
    }

    // Getters and setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
    public String getCurrentMenu() { return currentMenu; }
    public void setCurrentMenu(String currentMenu) { this.currentMenu = currentMenu; }
    public String getRecipientMsisdn() { return recipientMsisdn; }
    public void setRecipientMsisdn(String recipientMsisdn) { this.recipientMsisdn = recipientMsisdn; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

// Request DTO
class UssdRequest {
    private String sessionId;
    private String msisdn;
    private String userInput;
    private String serviceCode;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
    public String getUserInput() { return userInput; }
    public void setUserInput(String userInput) { this.userInput = userInput; }
    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }
}

// Response DTO
class UssdResponse {
    private String message;
    private boolean endSession;

    public static UssdResponse continueSession(String message) {
        UssdResponse response = new UssdResponse();
        response.message = message;
        response.endSession = false;
        return response;
    }

    public static UssdResponse endSession(String message) {
        UssdResponse response = new UssdResponse();
        response.message = message;
        response.endSession = true;
        return response;
    }

    public String getMessage() { return message; }
    public boolean isEndSession() { return endSession; }
}