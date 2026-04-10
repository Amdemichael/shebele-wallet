package com.shebele.wallet.repository;

import com.shebele.wallet.domain.UssdSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UssdSessionRepository extends JpaRepository<UssdSession, String> {

    Optional<UssdSession> findBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UssdSession s WHERE s.lastUpdatedAt < :timeout")
    int deleteExpiredSessions(@Param("timeout") LocalDateTime timeout);  // Changed void → int

    long countByStatus(UssdSession.SessionStatus status);
}