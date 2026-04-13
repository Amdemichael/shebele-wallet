package com.shebele.wallet.application.account.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterAccountCommand {
    String msisdn;
    String fullName;
}