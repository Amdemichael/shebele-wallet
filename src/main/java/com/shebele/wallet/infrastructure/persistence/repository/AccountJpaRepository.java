package com.shebele.wallet.infrastructure.persistence.repository;

import com.shebele.wallet.infrastructure.persistence.entity.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountJpaEntity, Long> {
    Optional<AccountJpaEntity> findByMsisdn(String msisdn);
    boolean existsByMsisdn(String msisdn);
}