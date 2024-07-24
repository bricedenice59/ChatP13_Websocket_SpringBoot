package com.openclassrooms.chatPOC.models.enums;

public enum RoleName {
    ADMIN("Admin"),
    USER("User");

    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static RoleName fromDisplayName(String displayName) {
        for (RoleName role : RoleName.values()) {
            if (role.getDisplayName().equals(displayName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role with display name " + displayName + " found.");
    }
}
