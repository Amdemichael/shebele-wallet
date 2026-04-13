package com.shebele.wallet.infrastructure.persistence.adapter;

import com.shebele.wallet.domain.model.TransactionEntry;
import com.shebele.wallet.domain.repository.TransactionEntryRepository;
import com.shebele.wallet.domain.valueobject.TransactionId;
import com.shebele.wallet.infrastructure.persistence.entity.TransactionEntryJpaEntity;
import com.shebele.wallet.infrastructure.persistence.mapper.TransactionEntryPersistenceMapper;
import com.shebele.wallet.infrastructure.persistence.repository.TransactionEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionEntryPersistenceAdapter implements TransactionEntryRepository {

    private final TransactionEntryJpaRepository jpaRepository;
    private final TransactionEntryPersistenceMapper mapper;

    @Override
    public void save(TransactionEntry entry) {
        TransactionEntryJpaEntity entity = mapper.toJpaEntity(entry);
        jpaRepository.save(entity);
    }

    @Override
    public void saveAll(List<TransactionEntry> entries) {
        List<TransactionEntryJpaEntity> entities = entries.stream()
                .map(mapper::toJpaEntity)
                .collect(Collectors.toList());
        jpaRepository.saveAll(entities);
    }

    @Override
    public Optional<TransactionEntry> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<TransactionEntry> findByTransactionId(TransactionId transactionId) {
        return jpaRepository.findByTransactionId(transactionId.getValue())
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntry> findByAccountMsisdn(String msisdn) {
        return jpaRepository.findByAccountMsisdnOrderByCreatedAtDesc(msisdn)
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionEntry> findByAccountMsisdnOrderByCreatedAtDesc(String msisdn) {
        return jpaRepository.findByAccountMsisdnOrderByCreatedAtDesc(msisdn)
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }
}