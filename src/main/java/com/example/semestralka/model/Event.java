package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Event extends AbstractEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @ManyToMany
    private List<Genre> genres;

    @OneToMany(mappedBy = "event")
    private List<Ticket> ticketsForEvent;
}
