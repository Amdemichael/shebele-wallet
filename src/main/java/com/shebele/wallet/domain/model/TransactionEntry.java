package com.shebele.wallet.domain.model;

import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.TransactionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntry {

    private TransactionId transactionId;
    private String accountMsisdn;
    private EntryType type;
    private Money amount;
    private String reference;
    private LocalDateTime createdAt;
    private EntryStatus status;

    public enum EntryType {
        DEBIT, CREDIT
    }

    public enum EntryStatus {
        PENDING, POSTED, REVERSED
    }

    // Factory method for new entries
    public static TransactionEntry create(TransactionId transactionId, String accountMsisdn,
                                          EntryType type, Money amount, String reference) {
        TransactionEntry entry = new TransactionEntry();
        entry.setTransactionId(transactionId);
        entry.setAccountMsisdn(accountMsisdn);
        entry.setType(type);
        entry.setAmount(amount);
        entry.setReference(reference);
        entry.setCreatedAt(LocalDateTime.now());
        entry.setStatus(EntryStatus.POSTED);
        return entry;
    }

    public boolean isDebit() {
        return type == EntryType.DEBIT;
    }

    public boolean isCredit() {
        return type == EntryType.CREDIT;
    }

    public void reverse() {
        this.status = EntryStatus.REVERSED;
    }
}