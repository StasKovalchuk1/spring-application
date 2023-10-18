package com.example.semestralka.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Genre extends AbstractEntity{
    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<Event> events;
}
