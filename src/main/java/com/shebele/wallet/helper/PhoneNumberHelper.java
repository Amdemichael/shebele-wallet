package com.shebele.wallet.helper;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

/**
 * Helper class for Ethiopian phone number operations.
 * Ethiopian numbers: 251 followed by 9 digits (e.g., 251911111111)
 */
@Component
public class PhoneNumberHelper {

    private static final Pattern ETHIOPIAN_PHONE_PATTERN = Pattern.compile("251[0-9]{9}");
    private static final Pattern ETHIOPIAN_PHONE_WITH_PLUS = Pattern.compile("\\+251[0-9]{9}");
    private static final Pattern ETHIOPIAN_PHONE_WITH_LEADING_ZERO = Pattern.compile("0[0-9]{9}");

    /**
     * Validates if a phone number is a valid Ethiopian number.
     * Accepts: 251911111111, +251911111111, 0911111111
     */
    public boolean isValidEthiopianPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        String normalized = normalizeToStandard(phoneNumber);
        return ETHIOPIAN_PHONE_PATTERN.matcher(normalized).matches();
    }

    /**
     * Normalizes phone number to standard format (251XXXXXXXXX)
     */
    public String normalizeToStandard(String phoneNumber) {
        if (phoneNumber == null) return null;

        // Remove all non-digit characters
        String cleaned = phoneNumber.replaceAll("[^0-9]", "");

        // Handle different formats
        if (cleaned.startsWith("251") && cleaned.length() == 12) {
            return cleaned;
        } else if (cleaned.startsWith("0") && cleaned.length() == 10) {
            return "251" + cleaned.substring(1);
        } else if (cleaned.length() == 9) {
            return "251" + cleaned;
        }

        return cleaned;
    }

    /**
     * Formats phone number for display (e.g., 251911111111 → 0911 111111)
     */
    public String formatForDisplay(String phoneNumber) {
        String normalized = normalizeToStandard(phoneNumber);
        if (normalized == null || normalized.length() != 12) {
            return phoneNumber;
        }

        return "0" + normalized.substring(3, 6) + " " + normalized.substring(6);
    }

    /**
     * Masks phone number for privacy (e.g., 251911111111 → 251****1111)
     */
    public String maskPhoneNumber(String phoneNumber) {
        String normalized = normalizeToStandard(phoneNumber);
        if (normalized == null || normalized.length() != 12) {
            return phoneNumber;
        }

        return normalized.substring(0, 3) + "****" + normalized.substring(7);
    }

    /**
     * Extracts the 9-digit local number from standard format
     */
    public String extractLocalNumber(String phoneNumber) {
        String normalized = normalizeToStandard(phoneNumber);
        if (normalized == null || normalized.length() != 12) {
            return phoneNumber;
        }

        return "0" + normalized.substring(3);
    }
}