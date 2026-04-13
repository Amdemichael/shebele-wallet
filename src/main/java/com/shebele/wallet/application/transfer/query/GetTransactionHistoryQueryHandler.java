package com.shebele.wallet.application.transfer.query;

import com.shebele.wallet.application.transfer.port.in.GetTransactionHistoryQueryPort;
import com.shebele.wallet.domain.model.TransactionEntry;
import com.shebele.wallet.domain.repository.TransactionEntryRepository;
import com.shebele.wallet.domain.valueobject.TransactionId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetTransactionHistoryQueryHandler implements GetTransactionHistoryQueryPort {

    private final TransactionEntryRepository transactionEntryRepository;

    @Override
    public List<TransactionHistoryResult> execute(GetTransactionHistoryQuery query) {
        log.info("Getting transaction history for: {}", query.getMsisdn());

        List<TransactionEntry> entries = transactionEntryRepository
                .findByAccountMsisdnOrderByCreatedAtDesc(query.getMsisdn());

        return entries.stream()
                .filter(entry -> !entry.getAccountMsisdn().equals("OPERATOR_REVENUE"))
                .map(entry -> {
                    String type = entry.isDebit() ? "SENT" : "RECEIVED";
                    String counterparty = findCounterparty(entry);
                    BigDecimal transferAmount = findTransferAmount(entry);
                    BigDecimal fee = findFee(entry.getTransactionId());

                    return TransactionHistoryResult.builder()
                            .transactionRef(entry.getTransactionId().getValue())
                            .type(type)
                            .counterparty(counterparty)
                            .amount(transferAmount)
                            .fee(fee)
                            .description(entry.getReference())
                            .status(entry.getStatus().name())
                            .timestamp(entry.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String findCounterparty(TransactionEntry entry) {
        List<TransactionEntry> sameTransaction = transactionEntryRepository
                .findByTransactionId(entry.getTransactionId());

        for (TransactionEntry e : sameTransaction) {
            if (!e.getAccountMsisdn().equals(entry.getAccountMsisdn())
                    && !e.getAccountMsisdn().equals("OPERATOR_REVENUE")) {
                return e.getAccountMsisdn();
            }
        }

        // For deposits/withdrawals
        if (entry.isCredit() && entry.getReference() != null && entry.getReference().contains("deposit")) {
            return "SYSTEM";
        }
        return "UNKNOWN";
    }

    private BigDecimal findTransferAmount(TransactionEntry entry) {
        if (entry.isDebit()) {
            // For debit entries, find the corresponding credit to get actual transfer amount
            List<TransactionEntry> sameTransaction = transactionEntryRepository
                    .findByTransactionId(entry.getTransactionId());

            for (TransactionEntry e : sameTransaction) {
                if (e.isCredit() && !e.getAccountMsisdn().equals("OPERATOR_REVENUE")) {
                    return e.getAmount().getAmount();
                }
            }
        }
        return entry.getAmount().getAmount();
    }

    private BigDecimal findFee(TransactionId transactionId) {
        List<TransactionEntry> sameTransaction = transactionEntryRepository
                .findByTransactionId(transactionId);

        for (TransactionEntry e : sameTransaction) {
            if (e.getAccountMsisdn().equals("OPERATOR_REVENUE")) {
                return e.getAmount().getAmount();
            }
        }
        return BigDecimal.ZERO;
    }
}