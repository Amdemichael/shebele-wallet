package com.shebele.wallet.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_entries")
@Data
public class TransactionEntryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private String accountMsisdn;

    @Column(nullable = false)
    private String type;  // DEBIT or CREDIT

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String reference;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}