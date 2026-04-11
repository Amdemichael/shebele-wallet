package com.shebele.wallet.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * USSD Session entity - represents an interactive USSD session with a user.
 * Sessions are short-lived (typically 60 seconds timeout).
 */
@Entity
@Table(name = "ussd_sessions")
@Data
@NoArgsConstructor
public class UssdSession {

    @Id
    private String sessionId;

    @Column(nullable = false, length = 15)
    private String msisdn;

    @Column(nullable = false)
    private String currentMenu;

    private String recipientMsisdn;

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.ACTIVE;

    public UssdSession(String sessionId, String msisdn) {
        this.sessionId = sessionId;
        this.msisdn = msisdn;
        this.currentMenu = "MAIN";
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.status = SessionStatus.ACTIVE;
    }

    public void updateLastActivity() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void setCurrentMenu(String menu) {
        this.currentMenu = menu;
        updateLastActivity();
    }

    public void setRecipientMsisdn(String recipient) {
        this.recipientMsisdn = recipient;
        updateLastActivity();
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        updateLastActivity();
    }

    public boolean isExpired() {
        // USSD sessions expire after 60 seconds of inactivity
        return LocalDateTime.now().isAfter(lastUpdatedAt.plusSeconds(60));
    }

    public enum SessionStatus {
        ACTIVE, EXPIRED, COMPLETED
    }
}