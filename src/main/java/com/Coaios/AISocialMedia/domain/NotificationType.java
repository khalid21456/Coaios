package com.Coaios.AISocialMedia.domain;

public enum NotificationType {

    POST("Post"),
    COMMENT("Comment"),
    LIKE("Like");

    private final String label;

    NotificationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
