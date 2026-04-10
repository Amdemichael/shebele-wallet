package com.shebele.wallet.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank(message = "Sender phone number is required")
    @Pattern(regexp = "251[0-9]{9}", message = "Sender phone number must start with 251 followed by 9 digits")
    private String fromMsisdn;

    @NotBlank(message = "Recipient phone number is required")
    @Pattern(regexp = "251[0-9]{9}", message = "Recipient phone number must start with 251 followed by 9 digits")
    private String toMsisdn;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}