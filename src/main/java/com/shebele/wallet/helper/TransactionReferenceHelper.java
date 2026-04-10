package com.shebele.wallet.helper;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class TransactionReferenceHelper {

    private static final String PREFIX = "TXN";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Generates a unique transaction reference
     * Format: TXN20260410153000abc1
     */
    public String generateReference() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String random = UUID.randomUUID().toString().substring(0, 4);
        return PREFIX + timestamp + random;
    }

    /**
     * Generates USSD-specific idempotency key
     */
    public String generateUssdKey(String sessionId, String msisdn) {
        return "USSD_" + sessionId + "_" + msisdn + "_" + System.currentTimeMillis();
    }

    /**
     * Generates reference with custom prefix
     */
    public String generateReferenceWithPrefix(String prefix) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String random = UUID.randomUUID().toString().substring(0, 4);
        return prefix + timestamp + random;
    }

    /**
     * Generates account number
     * Format: SHE20260410153000
     */
    public String generateAccountNumber() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        return "SHE" + timestamp;
    }

    /**
     * Validates transaction reference format
     */
    public boolean isValidReference(String reference) {
        return reference != null && reference.matches("^[A-Z]{3}\\d{14}[a-z0-9]{4}$");
    }

    /**
     * Extracts timestamp from reference
     */
    public LocalDateTime extractTimestampFromReference(String reference) {
        if (reference == null || reference.length() < 17) {
            return null;
        }

        String timestampStr = reference.substring(3, 17);
        try {
            return LocalDateTime.parse(timestampStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }
}