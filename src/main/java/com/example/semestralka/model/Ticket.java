package com.example.semestralka.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket  extends AbstractEntity{

    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "archive_id")
    private Archive archive;
}
