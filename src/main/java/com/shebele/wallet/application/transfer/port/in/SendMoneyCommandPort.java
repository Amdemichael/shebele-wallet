package com.shebele.wallet.application.transfer.port.in;

import com.shebele.wallet.application.transfer.command.SendMoneyCommand;
import com.shebele.wallet.application.transfer.query.SendMoneyResult;

public interface SendMoneyCommandPort {
    SendMoneyResult execute(SendMoneyCommand command);
}