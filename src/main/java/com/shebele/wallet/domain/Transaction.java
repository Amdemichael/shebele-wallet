package com.shebele.wallet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionRef;

    @Column(name = "idempotency", unique = true)  // ← FIXED: Map to existing column
    private String idempotencyKey;  // Field name can stay the same

    @Column(nullable = false, length = 15)
    private String fromMsisdn;

    @Column(nullable = false, length = 15)
    private String toMsisdn;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, REVERSED
    }
}