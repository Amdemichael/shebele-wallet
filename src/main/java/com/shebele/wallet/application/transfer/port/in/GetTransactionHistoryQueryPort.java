package com.shebele.wallet.application.transfer.port.in;

import com.shebele.wallet.application.transfer.query.GetTransactionHistoryQuery;
import com.shebele.wallet.application.transfer.query.TransactionHistoryResult;
import java.util.List;

public interface GetTransactionHistoryQueryPort {
    List<TransactionHistoryResult> execute(GetTransactionHistoryQuery query);
}