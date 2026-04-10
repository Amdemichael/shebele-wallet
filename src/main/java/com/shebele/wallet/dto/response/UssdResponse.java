package com.shebele.wallet.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UssdResponse {
    private String message;
    private boolean endSession;
}