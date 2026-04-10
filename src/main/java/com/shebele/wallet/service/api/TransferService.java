package com.shebele.wallet.service.api;

import com.shebele.wallet.domain.Transaction;
import com.shebele.wallet.dto.response.TransferResult;

import java.math.BigDecimal;
import java.util.List;

public interface TransferService {

    TransferResult transfer(String idempotencyKey, String fromMsisdn,
                            String toMsisdn, BigDecimal amount, String description);

    List<Transaction> getTransactionHistory(String msisdn, int days);
}