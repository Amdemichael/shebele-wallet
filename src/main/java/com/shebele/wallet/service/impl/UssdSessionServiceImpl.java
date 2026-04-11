package com.shebele.wallet.service.impl;

import com.shebele.wallet.domain.UssdSession;
import com.shebele.wallet.repository.UssdSessionRepository;
import com.shebele.wallet.service.api.UssdSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UssdSessionServiceImpl implements UssdSessionService {

    private final UssdSessionRepository sessionRepository;

    @Transactional
    public UssdSession getOrCreateSession(String sessionId, String msisdn) {
        UssdSession session = sessionRepository.findById(sessionId).orElse(null);

        if (session == null) {
            log.info("Creating new USSD session: {} for MSISDN: {}", sessionId, msisdn);
            session = new UssdSession(sessionId, msisdn);
            session = sessionRepository.save(session);
        }

        // Check if session expired
        if (session.isExpired()) {
            log.info("Session expired: {}, creating new one", sessionId);
            sessionRepository.delete(session);
            session = new UssdSession(sessionId, msisdn);
            session = sessionRepository.save(session);
        }

        session.updateLastActivity();
        return session;
    }

    @Transactional
    public void updateSession(UssdSession session) {
        session.updateLastActivity();
        sessionRepository.save(session);
    }

    @Transactional
    public void endSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
        log.info("Ended USSD session: {}", sessionId);
    }

    @Scheduled(fixedDelay = 60000) // Run every minute
    @Transactional
    public void cleanupExpiredSessions() {
        LocalDateTime timeout = LocalDateTime.now().minusSeconds(60);
        sessionRepository.deleteExpiredSessions(timeout);
        log.info("Cleaned up expired USSD sessions");
    }
}