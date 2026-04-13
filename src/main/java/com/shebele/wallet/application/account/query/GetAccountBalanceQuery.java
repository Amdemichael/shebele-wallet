package com.shebele.wallet.application.account.query;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetAccountBalanceQuery {
    String msisdn;
}