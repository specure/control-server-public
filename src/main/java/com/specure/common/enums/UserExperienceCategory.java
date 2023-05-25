package com.specure.common.enums;

public enum UserExperienceCategory {

    SOCIAL_MEDIA("Social Media"),
    WEB_BROWSING("Web Browsing"),
    VIDEO("Video"),
    EMAIL_MESSAGING("Email / Messaging"),
    ONLINE_GAMING("Online Gaming"),
    VOIP("VoIP");

    private final String name;

    UserExperienceCategory(String name) {
        this.name = name;
    }
}
