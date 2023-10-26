package com.example.semestralka.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Person extends AbstractEntity{
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int phoneNumber;

    @Column(nullable = false)
    private String email;
}
