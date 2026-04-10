package com.shebele.wallet.mapper;

import com.shebele.wallet.dto.response.UssdResponse;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class UssdMapper {

    public UssdResponse toContinueResponse(String message) {
        return UssdResponse.builder()
                .message(message)
                .endSession(false)
                .build();
    }

    public UssdResponse toEndResponse(String message) {
        return UssdResponse.builder()
                .message(message)
                .endSession(true)
                .build();
    }

    public String formatMainMenu() {
        return "Welcome to Shebele Wallet\n" +
                "1. Send Money\n" +
                "2. Check Balance\n" +
                "3. My Account\n" +
                "4. Exit";
    }

    public String formatSendMoneyPrompt() {
        return "Enter recipient phone number:";
    }

    public String formatAmountPrompt() {
        return "Enter amount in Birr:";
    }

    public String formatInvalidPhoneError() {
        return "Invalid phone number.\nPlease enter a valid Ethiopian number (e.g., 251911111111):";
    }

    public String formatInvalidAmountError() {
        return "Invalid amount.\nEnter numbers only (e.g., 100):";
    }

    public String formatInvalidSelectionMessage() {
        return "Invalid selection. Please try again:\n" +
                "1. Send Money\n2. Check Balance\n3. My Account\n4. Exit";
    }

    public String formatGoodbyeMessage() {
        return "Thank you for using Shebele Wallet. Goodbye!";
    }

    public String formatAccountNotFoundMessage() {
        return "Account not found. Please register first.";
    }

    public String formatBalanceMessage(String msisdn, BigDecimal balance) {
        return String.format("Account: %s\nBalance: %.2f Birr", msisdn, balance);
    }

    public String formatAccountDetailsMessage(String fullName, String accountNumber, BigDecimal balance) {
        return String.format(
                "Name: %s\nAccount: %s\nBalance: %.2f Birr",
                fullName, accountNumber, balance
        );
    }

    public String formatTransferSuccessMessage(String recipientMsisdn, BigDecimal amount, String transactionRef) {
        return String.format(
                "Transfer successful!\nAmount: %.2f Birr\nTo: %s\nReference: %s",
                amount, recipientMsisdn, transactionRef
        );
    }

    public String formatTransferFailedError(String errorMessage) {
        return "Transfer failed: " + errorMessage;
    }

    public String formatTransferCancelledMessage() {
        return "Transfer cancelled. Thank you!";
    }
}