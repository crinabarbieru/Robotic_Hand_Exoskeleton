package com.exoapp.models;

public enum Role {
    ROLE_PATIENT(1),
    ROLE_SUPERVISOR(2);

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Role fromCode(int code) {
        for (Role r : Role.values()) {
            if (r.getCode() == code) return r;
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }

    @Override
    public String toString() {
        return String.valueOf(this.getCode());
    }
}

