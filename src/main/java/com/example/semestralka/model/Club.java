package com.example.semestralka.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Club extends AbstractEntity{

    @OneToMany(mappedBy = "club")
    public List<Event> event;
}
