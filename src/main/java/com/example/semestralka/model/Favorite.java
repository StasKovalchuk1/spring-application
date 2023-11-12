package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Favorite{

    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    public User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @MapsId("eventId")
    public Event event;
}
