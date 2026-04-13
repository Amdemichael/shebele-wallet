package com.shebele.wallet.domain.repository;

import com.shebele.wallet.domain.model.TransactionEntry;
import com.shebele.wallet.domain.valueobject.TransactionId;
import java.util.List;
import java.util.Optional;

public interface TransactionEntryRepository {
    void save(TransactionEntry entry);
    void saveAll(List<TransactionEntry> entries);
    Optional<TransactionEntry> findById(Long id);
    List<TransactionEntry> findByTransactionId(TransactionId transactionId);
    List<TransactionEntry> findByAccountMsisdn(String msisdn);
    List<TransactionEntry> findByAccountMsisdnOrderByCreatedAtDesc(String msisdn);
}