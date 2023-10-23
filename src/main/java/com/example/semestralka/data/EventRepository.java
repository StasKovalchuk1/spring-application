package com.example.semestralka.data;

import com.example.semestralka.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository  extends CrudRepository<Event, Integer> {

    List<Event> getAllByFinishedIsFalse();

}

