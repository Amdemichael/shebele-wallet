package com.shebele.wallet.mapper;

import com.shebele.wallet.domain.Transaction;
import com.shebele.wallet.dto.response.TransferResponse;
import com.shebele.wallet.enums.ChannelType;
import com.shebele.wallet.enums.TransactionStatus;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionMapper {

    public TransferResponse toTransferResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return TransferResponse.builder()
                .transactionRef(transaction.getTransactionRef())
                .status(transaction.getStatus().getDisplayName())  // Get display name from enum
                .amount(transaction.getAmount())
                .fromAccount(transaction.getFromMsisdn())
                .toAccount(transaction.getToMsisdn())
                .timestamp(transaction.getCreatedAt())
                .build();
    }

    public Transaction toEntity(String idempotencyKey, String fromMsisdn,
                                String toMsisdn, BigDecimal amount,
                                String description, ChannelType channelType) {

        Transaction transaction = Transaction.builder()
                .transactionRef(generateTransactionRef())
                .idempotencyKey(idempotencyKey)
                .fromMsisdn(fromMsisdn)
                .toMsisdn(toMsisdn)
                .amount(amount)
                .description(description)
                .status(TransactionStatus.PENDING)
                .channelType(channelType)
                .createdAt(LocalDateTime.now())
                .build();

        transaction.markCompleted(); // Mark as completed immediately for successful transfers
        return transaction;
    }

    private String generateTransactionRef() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
    }
}