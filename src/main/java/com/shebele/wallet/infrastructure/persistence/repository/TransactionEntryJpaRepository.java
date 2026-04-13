package com.shebele.wallet.infrastructure.persistence.repository;

import com.shebele.wallet.infrastructure.persistence.entity.TransactionEntryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionEntryJpaRepository extends JpaRepository<TransactionEntryJpaEntity, Long> {
    List<TransactionEntryJpaEntity> findByAccountMsisdnOrderByCreatedAtDesc(String accountMsisdn);
    List<TransactionEntryJpaEntity> findByTransactionId(String transactionId);
}