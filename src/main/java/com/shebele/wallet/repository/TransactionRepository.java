package com.shebele.wallet.repository;

import com.shebele.wallet.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Fixed: Use @Query instead of method naming to avoid parsing issues
    @Query("SELECT t FROM Transaction t WHERE t.idempotencyKey = :key")
    Optional<Transaction> findByIdempotencyKey(@Param("key") String idempotencyKey);

    Optional<Transaction> findByTransactionRef(String transactionRef);

    @Query("SELECT t FROM Transaction t WHERE (t.fromMsisdn = :msisdn OR t.toMsisdn = :msisdn) " +
            "AND t.createdAt >= :fromDate ORDER BY t.createdAt DESC")
    List<Transaction> findTransactionsForUserSince(
            @Param("msisdn") String msisdn,
            @Param("fromDate") LocalDateTime fromDate);
}