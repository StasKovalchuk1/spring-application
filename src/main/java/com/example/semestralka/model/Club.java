package com.example.semestralka.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Club extends AbstractEntity{

    @Column(nullable = false)
    public String name;

    @OneToMany(mappedBy = "club")
    public List<Event> events;

    public void addEvent(Event event){
        if (events == null) events = new ArrayList<>();
        events.add(event);
    }
}
