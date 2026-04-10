package com.shebele.wallet.enums;

public enum ChannelType {
    USSD("USSD", "Unstructured Supplementary Service Data"),
    MOBILE_APP("Mobile App", "iOS/Android Application"),
    WEB("Web", "Web Browser"),
    API("API", "Partner API Integration"),
    AGENT_PORTAL("Agent Portal", "Agent Management Portal");

    private final String displayName;
    private final String description;

    ChannelType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}