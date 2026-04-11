package com.shebele.wallet.dto.request;

import lombok.Data;

@Data
public class UssdRequest {
    private String sessionId;
    private String msisdn;
    private String userInput;
    private String serviceCode;
}