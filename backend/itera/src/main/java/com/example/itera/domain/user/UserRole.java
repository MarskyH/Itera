package com.example.itera.domain.user;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (UserRole userRole : UserRole.values()) {
                if (userRole.role.equalsIgnoreCase(role)) {
                    return userRole;
                }
            }
            throw new IllegalArgumentException("No enum constant UserRole." + role);
        }
    }
}