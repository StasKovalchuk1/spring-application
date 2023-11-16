package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private boolean accepted = false;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(name = "event_genre")
    private List<Genre> genres;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "club_id")
    public Club club;

    public void addGenre(Genre genre) {
        if (this.genres == null) this.genres = new ArrayList<>();
        this.genres.add(genre);
    }

    public void addComment(Comment comment) {
        if (comments == null) comments = new ArrayList<>();
        this.comments.add(comment);
    }

    public void removeComment(Comment comment){
        this.comments.remove(comment);
    }
    public void removeFromClub() {
        if (this.club != null) {
            this.club.removeEvent(this);
            this.club = null;
        }
    }
 }
