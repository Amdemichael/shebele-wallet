package com.shebele.wallet.service.impl;

import com.shebele.wallet.domain.Transaction;
import com.shebele.wallet.dto.response.TransferResult;
import com.shebele.wallet.enums.ChannelType;
import com.shebele.wallet.enums.TransactionStatus;
import com.shebele.wallet.helper.AmountHelper;
import com.shebele.wallet.helper.TransactionReferenceHelper;
import com.shebele.wallet.repository.TransactionRepository;
import com.shebele.wallet.service.api.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final AccountServiceImpl accountService;
    private final TransactionRepository transactionRepository;
    private final AmountHelper amountHelper;  // Inject helper
    private final TransactionReferenceHelper referenceHelper;  // Inject helper

    @Transactional(rollbackFor = Exception.class)
    public TransferResult transfer(String idempotencyKey, String fromMsisdn,
                                   String toMsisdn, BigDecimal amount, String description) {

        // 1. Idempotency check - Return DUPLICATE status if key exists
        if (idempotencyKey != null && !idempotencyKey.isEmpty()) {
            var existing = transactionRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                log.info("Duplicate request detected with idempotency key: {}", idempotencyKey);
                return TransferResult.duplicate(existing.get());
            }
        }

        // 2. Validate amount
        if (!amountHelper.isValidAmount(amount)) {
            return TransferResult.failed(
                    "Amount must be between " + amountHelper.getMinimumTransfer() +
                            " and " + amountHelper.getMaximumTransfer() + " Birr"
            );
        }

        // 3. Validate not same account
        if (fromMsisdn.equals(toMsisdn)) {
            throw new RuntimeException("Cannot transfer to same account");
        }

        // 4. Lock accounts in consistent order to prevent deadlock
        String firstAccount = fromMsisdn.compareTo(toMsisdn) < 0 ? fromMsisdn : toMsisdn;
        String secondAccount = fromMsisdn.compareTo(toMsisdn) < 0 ? toMsisdn : fromMsisdn;

        accountService.getAccount(firstAccount);
        accountService.getAccount(secondAccount);

        // 5. Execute transfer
        accountService.updateBalance(fromMsisdn, amount, false);  // Debit sender
        accountService.updateBalance(toMsisdn, amount, true);     // Credit receiver

        // 6. Record transaction
        String transactionRef = referenceHelper.generateReference();
        Transaction transaction = Transaction.builder()
                .transactionRef(transactionRef)
                .idempotencyKey(idempotencyKey)
                .fromMsisdn(fromMsisdn)
                .toMsisdn(toMsisdn)
                .amount(amount)
                .description(description)
                .status(TransactionStatus.COMPLETED)  // ← FIXED: Use enum directly
                .channelType(ChannelType.API)          // ← Added channel type
                .createdAt(LocalDateTime.now())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transfer completed: {} from {} to {}. Ref: {}",
                amount, fromMsisdn, toMsisdn, transactionRef);

        return TransferResult.success(saved);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(String msisdn, int days) {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
        return transactionRepository.findTransactionsForUserSince(msisdn, fromDate);
    }

}