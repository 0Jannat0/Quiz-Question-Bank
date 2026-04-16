package com.quizbank;

public enum Topic {
    MATH("Math"),
    HISTORY("History"),
    SCIENCE("Science"),
    JAVA("Java Programming"),
    GENERAL("General Knowledge");

    private final String displayName;

    Topic(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Topic fromString(String text) {
        for (Topic t : Topic.values()) {
            if (t.displayName.equalsIgnoreCase(text)) {
                return t;
            }
        }
        return GENERAL;
    }
}