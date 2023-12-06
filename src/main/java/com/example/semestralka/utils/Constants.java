package com.example.semestralka.utils;

import com.example.semestralka.model.Role;

public class Constants {
    /**
     * Default user role.
     */
    public static final Role DEFAULT_ROLE = Role.USER;

    /**
     * Username login form parameter.
     */
    public static final String USERNAME_PARAM = "username";

    private Constants() {
        throw new AssertionError();
    }
}
