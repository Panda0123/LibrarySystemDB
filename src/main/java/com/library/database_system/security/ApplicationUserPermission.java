package com.library.database_system.security;

public enum ApplicationUserPermission {
    // permissions
    BOOK_WRITE("book:write"),
    AUTHOR_WRITE("author:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
