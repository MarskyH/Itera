package com.example.itera.enumeration;

import lombok.Getter;

@Getter
public enum ResponseType {
    SUCCESS_SAVE("Data saved successfully"),
    FAIL_SAVE("Data not saved"),
    SUCCESS_UPDATE("Data updated successfully"),
    FAIL_UPDATE("Data not updated"),
    SUCCESS_DELETE("Data deleted successfully"),
    FAIL_DELETE("Data not deleted"),
    EMPTY_GET("No data found in the database"),
    EMPTY_USER("No data user found in the database");
    private final String message;

    private ResponseType(String message) {
        this.message = message;
    }
}
