package com.shebele.wallet.infrastructure.persistence.mapper;

import com.shebele.wallet.domain.model.Account;
import com.shebele.wallet.domain.valueobject.AccountId;
import com.shebele.wallet.domain.valueobject.Money;
import com.shebele.wallet.domain.valueobject.PhoneNumber;
import com.shebele.wallet.infrastructure.persistence.entity.AccountJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountPersistenceMapper {

    AccountPersistenceMapper INSTANCE = Mappers.getMapper(AccountPersistenceMapper.class);

    @Mapping(target = "id", source = "databaseId")
    @Mapping(target = "domainId", source = "id", qualifiedByName = "accountIdToString")
    @Mapping(target = "msisdn", source = "msisdn", qualifiedByName = "phoneNumberToString")
    @Mapping(target = "balance", source = "balance", qualifiedByName = "moneyToBigDecimal")
    @Mapping(target = "status", source = "status")
    AccountJpaEntity toJpaEntity(Account account);

    @Mapping(target = "databaseId", source = "id")
    @Mapping(target = "id", source = "domainId", qualifiedByName = "stringToAccountId")
    @Mapping(target = "msisdn", source = "msisdn", qualifiedByName = "stringToPhoneNumber")
    @Mapping(target = "balance", source = "balance", qualifiedByName = "bigDecimalToMoney")
    @Mapping(target = "status", source = "status")
    Account toDomainEntity(AccountJpaEntity entity);

    @Named("accountIdToString")
    default String accountIdToString(AccountId accountId) {
        return accountId != null ? accountId.getValue() : null;
    }

    @Named("stringToAccountId")
    default AccountId stringToAccountId(String value) {
        return value != null ? AccountId.of(value) : null;
    }

    @Named("phoneNumberToString")
    default String phoneNumberToString(PhoneNumber phoneNumber) {
        return phoneNumber != null ? phoneNumber.getValue() : null;
    }

    @Named("stringToPhoneNumber")
    default PhoneNumber stringToPhoneNumber(String value) {
        return value != null ? PhoneNumber.of(value) : null;
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