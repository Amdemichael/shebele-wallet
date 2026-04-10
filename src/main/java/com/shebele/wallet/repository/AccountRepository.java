package com.shebele.wallet.repository;

import com.shebele.wallet.domain.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByMsisdn(String msisdn);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.msisdn = :msisdn")
    Optional<Account> findByMsisdnWithLock(@Param("msisdn") String msisdn);

    // FIXED: 'exists' not 'exist' (with an 's')
    boolean existsByMsisdn(String msisdn);
}