package com.example.semestralka.model;

import lombok.Data;

public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
