package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "EAR_USER")
public class User extends AbstractEntity{
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int phoneNumber;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    private Archive order;
}
