package com.shebele.wallet.application.transfer.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetTransactionHistoryQuery {
    String msisdn;
    int days;
}