package com.shebele.wallet.infrastructure.persistence.mapper;

import com.shebele.wallet.domain.model.TransactionEntry;
import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.TransactionId;
import com.shebele.wallet.infrastructure.persistence.entity.TransactionEntryJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionEntryPersistenceMapper {

    TransactionEntryPersistenceMapper INSTANCE = Mappers.getMapper(TransactionEntryPersistenceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactionId", source = "transactionId", qualifiedByName = "transactionIdToString")
    @Mapping(target = "accountMsisdn", source = "accountMsisdn")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "moneyToBigDecimal")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    TransactionEntryJpaEntity toJpaEntity(TransactionEntry entry);

    @Mapping(target = "transactionId", source = "transactionId", qualifiedByName = "stringToTransactionId")
    @Mapping(target = "accountMsisdn", source = "accountMsisdn")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "amount", source = "amount", qualifiedByName = "bigDecimalToMoney")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    TransactionEntry toDomainEntity(TransactionEntryJpaEntity entity);

    @Named("transactionIdToString")
    default String transactionIdToString(TransactionId transactionId) {
        return transactionId != null ? transactionId.getValue() : null;
    }

    @Named("stringToTransactionId")
    default TransactionId stringToTransactionId(String value) {
        return value != null ? TransactionId.of(value) : null;
    }

    @Named("moneyToBigDecimal")
    default java.math.BigDecimal moneyToBigDecimal(Money money) {
        return money != null ? money.getAmount() : null;
    }

    @Named("bigDecimalToMoney")
    default Money bigDecimalToMoney(java.math.BigDecimal value) {
        return value != null ? Money.of(value) : null;
    }
}