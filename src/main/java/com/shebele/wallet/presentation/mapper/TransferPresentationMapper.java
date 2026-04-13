package com.shebele.wallet.presentation.mapper;

import com.shebele.wallet.application.transfer.command.SendMoneyCommand;
import com.shebele.wallet.application.transfer.query.SendMoneyResult;
import com.shebele.wallet.application.transfer.query.TransactionHistoryResult;
import com.shebele.wallet.presentation.dto.request.TransferMoneyRequest;
import com.shebele.wallet.presentation.dto.response.TransferMoneyResponse;
import com.shebele.wallet.presentation.dto.response.TransactionHistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface TransferPresentationMapper {

    TransferPresentationMapper INSTANCE = Mappers.getMapper(TransferPresentationMapper.class);

    @Mapping(target = "idempotencyKey", source = "idempotencyKey")
    @Mapping(target = "fromMsisdn", source = "request.fromMsisdn")
    @Mapping(target = "toMsisdn", source = "request.toMsisdn")
    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "description", source = "request.description")
    SendMoneyCommand toCommand(TransferMoneyRequest request, String idempotencyKey);

    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    TransferMoneyResponse toResponse(SendMoneyResult result);

    TransactionHistoryResponse toHistoryResponse(TransactionHistoryResult result);
}