package com.shebele.wallet.service.api;

import com.shebele.wallet.domain.UssdSession;

public interface UssdSessionService {

    UssdSession getOrCreateSession(String sessionId, String msisdn);

    void updateSession(UssdSession session);

    void endSession(String sessionId);

    void cleanupExpiredSessions();
}