package com.example.semestralka.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Archive extends AbstractEntity{
    @OneToMany
    private List<Ticket> ticketsInArchive;
}
