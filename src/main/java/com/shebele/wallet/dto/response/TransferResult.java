package com.shebele.wallet.dto.response;

import com.shebele.wallet.domain.Transaction;
import lombok.Getter;

/**
 * Transfer result wrapper - returned by TransferService.
 * Contains either success result or error information.
 */
@Getter
public class TransferResult {
    private final boolean success;
    private final boolean duplicate;
    private final Transaction transaction;
    private final String errorMessage;

    private TransferResult(boolean success, boolean duplicate, Transaction transaction, String errorMessage) {
        this.success = success;
        this.duplicate = duplicate;
        this.transaction = transaction;
        this.errorMessage = errorMessage;
    }

    public static TransferResult success(Transaction transaction) {
        return new TransferResult(true, false, transaction, null);
    }

    public static TransferResult duplicate(Transaction existingTransaction) {
        return new TransferResult(false, true, existingTransaction, "Duplicate request - already processed");
    }

    public static TransferResult failed(String errorMessage) {
        return new TransferResult(false, false, null, errorMessage);
    }

    public String getTransactionRef() {
        return transaction != null ? transaction.getTransactionRef() : null;
    }
}