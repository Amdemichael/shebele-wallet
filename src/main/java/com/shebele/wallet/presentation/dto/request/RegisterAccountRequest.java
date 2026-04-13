package com.shebele.wallet.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterAccountRequest {
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "251[0-9]{9}", message = "Phone number must start with 251 followed by 9 digits")
    private String msisdn;

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
}