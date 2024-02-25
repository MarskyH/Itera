package com.example.itera.domain.user;

public enum UserRole {
    ADMIN("admin"),
    USER("suer"),
    ;

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
