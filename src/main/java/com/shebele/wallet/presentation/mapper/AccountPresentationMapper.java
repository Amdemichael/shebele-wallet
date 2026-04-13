package com.shebele.wallet.presentation.mapper;

import com.shebele.wallet.application.account.command.DepositMoneyCommand;
import com.shebele.wallet.application.account.command.RegisterAccountCommand;
import com.shebele.wallet.application.account.query.AccountBalanceResult;
import com.shebele.wallet.application.account.query.AccountDetailsResult;
import com.shebele.wallet.application.account.query.DepositMoneyResult;
import com.shebele.wallet.presentation.dto.request.DepositMoneyRequest;
import com.shebele.wallet.presentation.dto.request.RegisterAccountRequest;
import com.shebele.wallet.presentation.dto.response.AccountBalanceResponse;
import com.shebele.wallet.presentation.dto.response.AccountDetailsResponse;
import com.shebele.wallet.presentation.dto.response.DepositMoneyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface AccountPresentationMapper {

    AccountPresentationMapper INSTANCE = Mappers.getMapper(AccountPresentationMapper.class);

    // Request → Command
    RegisterAccountCommand toCommand(RegisterAccountRequest request);

    @Mapping(target = "msisdn", source = "msisdn")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "reference", source = "request.reference")
    DepositMoneyCommand toCommand(String msisdn, DepositMoneyRequest request);

    // Result → Response
    AccountDetailsResponse toResponse(AccountDetailsResult result);

    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    AccountBalanceResponse toBalanceResponse(AccountBalanceResult result);

    @Mapping(target = "message", constant = "Deposit successful")
    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    DepositMoneyResponse toDepositResponse(DepositMoneyResult result);
}