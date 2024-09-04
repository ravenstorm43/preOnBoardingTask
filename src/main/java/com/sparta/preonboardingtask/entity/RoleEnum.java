package com.sparta.preonboardingtask.entity;

public enum RoleEnum {
    ROLE_USER(Authority.ROLE_USER),
    ROLE_ADMIN(Authority.ROLE_ADMIN);
    private final String authority;

    public String getAuthority() {
        return this.authority;
    }
    RoleEnum(String authority) {
        this.authority = authority;
    }
    public static class Authority {
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}