package com.shebele.wallet.infrastructure.persistence.adapter;

import com.shebele.wallet.application.port.out.AccountPersistencePort;
import com.shebele.wallet.domain.model.Account;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import com.shebele.wallet.infrastructure.persistence.entity.AccountJpaEntity;
import com.shebele.wallet.infrastructure.persistence.mapper.AccountPersistenceMapper;
import com.shebele.wallet.infrastructure.persistence.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountPersistencePort {

    private final AccountJpaRepository jpaRepository;
    private final AccountPersistenceMapper mapper;

    @Override
    public Optional<Account> findByMsisdn(PhoneNumber msisdn) {
        return jpaRepository.findByMsisdn(msisdn.getValue())
                .map(mapper::toDomainEntity);
    }

    @Override
    public void save(Account account) {
        AccountJpaEntity entity = mapper.toJpaEntity(account);
        AccountJpaEntity saved = jpaRepository.save(entity);

        // IMPORTANT: Update domain entity with database-generated ID
        // This ensures subsequent saves will UPDATE instead of INSERT
        if (account.getDatabaseId() == null) {
            account.setDatabaseId(saved.getId());
        }
    }

    @Override
    public boolean existsByMsisdn(PhoneNumber msisdn) {
        return jpaRepository.existsByMsisdn(msisdn.getValue());
    }
}