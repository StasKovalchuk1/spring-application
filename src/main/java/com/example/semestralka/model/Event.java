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

    @Column(nullable = false)
    private boolean isFinished = false;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(name = "event_genre")
    private List<Genre> genres;

    @ManyToOne
    @JoinColumn(name = "club_id")
    public Club club;
}
