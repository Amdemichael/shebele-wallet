package com.shebele.wallet.service.api;

import com.shebele.wallet.domain.Account;
import java.math.BigDecimal;

public interface AccountService {

    Account createAccount(String msisdn, String fullName);

    BigDecimal getBalance(String msisdn);

    Account updateBalance(String msisdn, BigDecimal amount, boolean isCredit);

    Account getAccount(String msisdn);
}