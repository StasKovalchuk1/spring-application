package com.example.semestralka.data;

import com.example.semestralka.model.Event;
import com.example.semestralka.model.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository  extends CrudRepository<Event, Integer> {

    @Query("SELECT e FROM Event e WHERE e.isFinished = false")
    List<Ticket> getAllByIsFinishedFalse();
}
