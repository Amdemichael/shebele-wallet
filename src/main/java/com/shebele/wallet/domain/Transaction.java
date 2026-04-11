package com.shebele.wallet.domain;

import com.shebele.wallet.enums.ChannelType;
import com.shebele.wallet.enums.Currency;
import com.shebele.wallet.enums.TransactionStatus;
import com.shebele.wallet.enums.TransactionType;
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

    @Column(unique = true)
    private String idempotencyKey;

    @Column(nullable = false, length = 15)
    private String fromMsisdn;

    @Column(nullable = false, length = 15)
    private String toMsisdn;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.ETB;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType = TransactionType.P2P_TRANSFER;

    @Enumerated(EnumType.STRING)
    private ChannelType channelType = ChannelType.API;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    public void markCompleted() {
        this.status = TransactionStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = TransactionStatus.FAILED;
        this.completedAt = LocalDateTime.now();
    }
}