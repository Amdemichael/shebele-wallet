package com.shebele.wallet.helper;

import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Helper class for idempotency key operations.
 */
@Component
public class IdempotencyHelper {

    /**
     * Generates a new idempotency key (UUID format)
     */
    public String generateKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates an idempotency key for a specific channel
     */
    public String generateKeyForChannel(String channel, String identifier) {
        return channel.toUpperCase() + "_" + identifier + "_" + System.currentTimeMillis();
    }

    /**
     * Generates USSD-specific idempotency key
     */
    public String generateUssdKey(String sessionId, String msisdn) {
        return "USSD_" + sessionId + "_" + msisdn + "_" + System.currentTimeMillis();
    }

    /**
     * Generates API-specific idempotency key
     */
    public String generateApiKey(String apiKey, String requestId) {
        return "API_" + apiKey + "_" + requestId;
    }

    /**
     * Validates idempotency key format
     */
    public boolean isValidKey(String idempotencyKey) {
        return idempotencyKey != null && !idempotencyKey.isEmpty() && idempotencyKey.length() <= 255;
    }

    /**
     * Extracts channel from idempotency key
     */
    public String extractChannel(String idempotencyKey) {
        if (idempotencyKey == null || !idempotencyKey.contains("_")) {
            return "UNKNOWN";
        }
        return idempotencyKey.split("_")[0];
    }
}