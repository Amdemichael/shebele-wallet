// presentation/dto/request/TransferMoneyRequest.java
package com.shebele.wallet.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferMoneyRequest {

    @NotBlank
    @Pattern(regexp = "251[0-9]{9}")
    private String fromMsisdn;

    @NotBlank
    @Pattern(regexp = "251[0-9]{9}")
    private String toMsisdn;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;
}