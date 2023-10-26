package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Genre extends AbstractEntity{
    @Column(nullable = false)
    private String name;

    @Override
    public String toString() {
        return "Genre{" +
                "name='" + name + '\'' +
                "}";
    }

}
