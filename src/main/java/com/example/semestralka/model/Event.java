package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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
    private boolean finished = false;

    @Column(nullable = false)
    private boolean accepted = false;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(name = "event_genre")
    private List<Genre> genres;

    @ManyToOne
    @JoinColumn(name = "club_id")
    public Club club;

    public void addGenre(Genre genre) {
        if (genres == null) {genres = new ArrayList<>();}
        genres.add(genre);
    }
}
